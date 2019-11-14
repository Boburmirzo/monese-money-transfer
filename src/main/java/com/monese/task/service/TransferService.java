package com.monese.task.service;

import com.monese.task.dto.TransferDTO;
import com.monese.task.entity.TransactionEntity;
import com.monese.task.exception.RuntimeException;
import java.util.List;

public interface TransferService {
  TransferDTO transfer(TransferDTO transferDTO) throws RuntimeException;

  List<TransferDTO> findAll();
}
