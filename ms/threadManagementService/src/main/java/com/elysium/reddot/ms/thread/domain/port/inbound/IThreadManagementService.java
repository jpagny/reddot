package com.elysium.reddot.ms.thread.domain.port.inbound;

import com.elysium.reddot.ms.thread.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

import java.util.List;

public interface IThreadManagementService {

    ThreadModel getThreadById(Long id);

    List<ThreadModel> getAllThreads();

    ThreadModel createThread(ThreadModel threadModel) throws FieldEmptyException;

    ThreadModel updateThread(Long id, ThreadModel threadModel);

    ThreadModel deleteThreadById(Long id);
    
}
