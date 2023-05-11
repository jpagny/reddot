package com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.outbound.ITUserMessageStatisticRepository;
import com.elysium.reddot.ms.statistic.infrastructure.mapper.UserMessageStatisticPersistenceMapper;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.repository.UserMessageStatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMessageStatisticRepositoryAdapter implements ITUserMessageStatisticRepository {

    private final UserMessageStatisticRepository userMessageStatisticRepository;

    @Override
    public void createUserMessageStatistic(UserMessageStatisticModel userMessageStatisticModel) {
        UserMessageStatisticJpaEntity userMessageStatisticJpaEntity = UserMessageStatisticPersistenceMapper.toEntity(userMessageStatisticModel);
        log.debug("Save ici : " + userMessageStatisticJpaEntity.toString());
        userMessageStatisticRepository.save(userMessageStatisticJpaEntity);
    }

    @Override
    public Integer getCountMessagesByTypeAndUserIdAndDate(String type, String userId, LocalDate date){
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        return userMessageStatisticRepository.getCountMessagesByTypeAndUserIdAndDate(type,userId,sqlDate);
    }


}
