package com.takirahal.srfgroup.modules.offer.services.impl;

import com.takirahal.srfgroup.exceptions.BadRequestAlertException;
import com.takirahal.srfgroup.exceptions.UnauthorizedException;
import com.takirahal.srfgroup.modules.favoriteuser.entities.FavoriteUser;
import com.takirahal.srfgroup.modules.favoriteuser.repositories.FavoriteUserRepository;
import com.takirahal.srfgroup.modules.notification.dto.NotificationDTO;
import com.takirahal.srfgroup.modules.notification.enums.ModuleNotification;
import com.takirahal.srfgroup.modules.notification.services.NotificationService;
import com.takirahal.srfgroup.modules.offer.dto.OfferImagesDTO;
import com.takirahal.srfgroup.modules.offer.dto.RentOfferDTO;
import com.takirahal.srfgroup.modules.offer.entities.RentOffer;
import com.takirahal.srfgroup.modules.offer.dto.filter.RentOfferFilter;
import com.takirahal.srfgroup.modules.offer.mapper.RentOfferMapper;
import com.takirahal.srfgroup.modules.offer.repositories.OfferImagesRepository;
import com.takirahal.srfgroup.modules.offer.repositories.RentOfferRepository;
import com.takirahal.srfgroup.modules.offer.services.OfferImagesService;
import com.takirahal.srfgroup.modules.offer.services.RentOfferService;
import com.takirahal.srfgroup.modules.user.entities.UserOneSignal;
import com.takirahal.srfgroup.modules.user.exceptioins.AccountResourceException;
import com.takirahal.srfgroup.modules.user.mapper.UserMapper;
import com.takirahal.srfgroup.modules.user.services.UserOneSignalService;
import com.takirahal.srfgroup.security.UserPrincipal;
import com.takirahal.srfgroup.services.impl.OneSignalService;
import com.takirahal.srfgroup.services.impl.StorageService;
import com.takirahal.srfgroup.utils.CommonUtil;
import com.takirahal.srfgroup.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RentOfferServiceImpl implements RentOfferService {

    private final Logger log = LoggerFactory.getLogger(RentOfferServiceImpl.class);

    @Autowired
    RentOfferMapper rentOfferMapper;

    @Autowired
    RentOfferRepository rentOfferRepository;

    @Autowired
    OfferImagesService offerImagesService;

    @Autowired
    OfferImagesRepository offerImagesRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FavoriteUserRepository favoriteUserRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserOneSignalService userOneSignalService;

    @Autowired
    OneSignalService oneSignalService;

    @Autowired
    MessageSource messageSource;

    @Override
    public RentOfferDTO save(RentOfferDTO rentOfferDTO) {
        log.info("Request to save RentOffer : {}", rentOfferDTO);

        if (rentOfferDTO.getId() != null) {
            throw new BadRequestAlertException("A new rentOffer cannot already have an ID idexists");
        }

        UserPrincipal currentUser = SecurityUtils.getCurrentUser().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        rentOfferDTO.setUser(userMapper.toCurrentUserPrincipal(currentUser));
        rentOfferDTO.setBlockedByReported(Boolean.FALSE);
        rentOfferDTO.setDateCreated(Instant.now());
        RentOffer rentOffer = rentOfferMapper.toEntity(rentOfferDTO);
        rentOffer = rentOfferRepository.save(rentOffer);
        RentOfferDTO result = rentOfferMapper.toDto(rentOffer);

        for (OfferImagesDTO offerImagesDTO : rentOfferDTO.getOfferImages()) {
            offerImagesDTO.setOffer(result);
            offerImagesService.save(offerImagesDTO);
        }

        // Create Notification and send push notification for favorite users
        createNotificationsForFavoriteUsers();

        // Save Els
        // Post post = new Post(result.getId().toString(), result.getTitle(), result.getDescription(), ModulePost.offer.toString());
        // postService.save(post);

        return result;
    }

    @Override
    public Page<RentOfferDTO> findByCriteria(RentOfferFilter rentOfferFilter, Pageable pageable) {
        return rentOfferRepository.findAll(createSpecification(rentOfferFilter), pageable).map(rentOfferMapper::toDto);
    }

    @Override
    public RentOfferDTO updateRentOffer(RentOfferDTO rentOfferDTO, Long id) {
        log.debug("Request to update RentOffer : {}", rentOfferDTO);
        if (rentOfferDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id idnull");
        }
        if (!Objects.equals(id, rentOfferDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID idinvalid");
        }

        RentOffer rentOffer = rentOfferRepository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException("Entity not found with id"));

        Long useId = SecurityUtils
                .getIdByCurrentUser();
        if (!Objects.equals(useId, rentOfferDTO.getUser().getId())) {
            throw new UnauthorizedException("Unauthorized action");
        }

        rentOffer = rentOfferMapper.toEntity(rentOfferDTO);
        RentOffer rentOfferUpdate = rentOfferRepository.save(rentOffer);
        RentOfferDTO newSellOfferDTO = rentOfferMapper.toDto(rentOfferUpdate);


        // Any modif for images
        if( rentOfferDTO.getOfferImages().size()> 0 ){

            // Delete folder for images
            String pathAddProduct = storageService.getBaseStorageProductImages() + id;
            if (storageService.existPath(pathAddProduct)) {
                Path rootLocation = Paths.get(pathAddProduct);
                storageService.deleteFiles(rootLocation);
            }

            // Delete old images
            offerImagesRepository.deleteImagesByOfferId(newSellOfferDTO.getId());
        }

        // Update images for offer
        for (OfferImagesDTO offerImagesDTO : rentOfferDTO.getOfferImages()) {
            offerImagesDTO.setOffer(newSellOfferDTO);
            offerImagesService.save(offerImagesDTO);
        }

        return newSellOfferDTO;
    }

    @Async
    public void createNotificationsForFavoriteUsers(){
        List<FavoriteUser> favoriteUserList = favoriteUserRepository.findYourFavoriteUsers();
        for ( FavoriteUser favoriteUser: favoriteUserList ) {

            Locale locale = Locale.forLanguageTag(!favoriteUser.getCurrentUser().getLangKey().equals("") ? favoriteUser.getCurrentUser().getLangKey() : "fr");
            String messageCommentOffer = CommonUtil.getFullNameUser(userMapper.toDto(favoriteUser.getFavoriteUser()))+" "+messageSource.getMessage("offer.message_favorite_user_add_new_offer", null, locale);

            // Create notification
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setDateCreated(Instant.now());
            notificationDTO.setContent(messageCommentOffer);
            notificationDTO.setModule(ModuleNotification.OfferNotification.name());
            notificationDTO.setIsRead(Boolean.FALSE);
            notificationDTO.setUser(userMapper.toDto(favoriteUser.getCurrentUser()));
            notificationService.save(notificationDTO);

            // Send Push Notif
            List<UserOneSignal> listUserOneSignals = userOneSignalService.findOneSignalByUser(favoriteUser.getCurrentUser());
            if(listUserOneSignals.size()>0){
                String result = listUserOneSignals.stream().map(UserOneSignal::getIdOneSignal)
                        .collect(Collectors.joining(","));
                oneSignalService.sendPushNotifToUser(result, messageCommentOffer);
            }
        }
    }

    private Specification<RentOffer> createSpecification(RentOfferFilter rentOfferFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
