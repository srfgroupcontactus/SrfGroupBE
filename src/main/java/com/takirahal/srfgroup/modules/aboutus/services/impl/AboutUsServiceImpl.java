package com.takirahal.srfgroup.modules.aboutus.services.impl;

import com.takirahal.srfgroup.exceptions.BadRequestAlertException;
import com.takirahal.srfgroup.modules.aboutus.dto.AboutUsDTO;
import com.takirahal.srfgroup.modules.aboutus.dto.filter.AboutUsFilter;
import com.takirahal.srfgroup.modules.aboutus.entities.AboutUs;
import com.takirahal.srfgroup.modules.aboutus.mapper.AboutUsMapper;
import com.takirahal.srfgroup.modules.aboutus.repositories.AboutUsRepository;
import com.takirahal.srfgroup.modules.aboutus.services.AboutUsService;
import com.takirahal.srfgroup.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AboutUsServiceImpl implements AboutUsService {

    private final Logger log = LoggerFactory.getLogger(AboutUsServiceImpl.class);

    @Autowired
    AboutUsMapper aboutUsMapper;

    @Autowired
    AboutUsRepository aboutUsRepository;

    @Override
    public AboutUsDTO save(AboutUsDTO aboutUsDTO) {
        log.debug("Request to save AboutUs : {}", aboutUsDTO);

        if (aboutUsDTO.getId() != null) {
            throw new BadRequestAlertException(RequestUtil.messageTranslate("common.entity_already_exist"));
        }

        AboutUs aboutUs = aboutUsMapper.toEntity(aboutUsDTO);
        aboutUs = aboutUsRepository.save(aboutUs);
        return aboutUsMapper.toDto(aboutUs);
    }

    @Override
    public Page<AboutUsDTO> findByCriteria(AboutUsFilter aboutUsFilter, Pageable pageable) {
        log.debug("Request to findByCriteria AboutUs : {}", aboutUsFilter);
        return aboutUsRepository.findAll(createSpecification(aboutUsFilter), pageable).map(aboutUsMapper::toDto);
    }

    @Override
    public Optional<AboutUsDTO> findLastOne() {
        log.debug("Request to findLastOne : {}");
        return aboutUsRepository.findTopByOrderByIdDesc().map(aboutUsMapper::toDto);
    }

    protected Specification<AboutUs> createSpecification(AboutUsFilter aboutUsFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
