package com.elysium.reddot.ms.statistic.infrastructure.persistence;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.outbound.ITUserMessageStatisticRepository;
import com.elysium.reddot.ms.statistic.infrastructure.mapper.UserMessageStatisticPersistenceMapper;
import com.elysium.reddot.ms.statistic.infrastructure.persistence.entity.UserMessageStatisticJpaEntity;
import com.elysium.reddot.ms.statistic.infrastructure.persistence.repository.UserMessageStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMessageStatisticRepositoryAdapter implements ITUserMessageStatisticRepository {

    private final UserMessageStatisticRepository userMessageStatisticRepository;

    @Override
    public void createUserMessageStatistic(UserMessageStatisticModel userMessageStatisticModel) {
        UserMessageStatisticJpaEntity userMessageStatisticJpaEntity = UserMessageStatisticPersistenceMapper.toEntity(userMessageStatisticModel);
        userMessageStatisticRepository.save(userMessageStatisticJpaEntity);
    }
}
