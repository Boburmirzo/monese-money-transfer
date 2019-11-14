package com.monese.task.mapper;

import com.monese.task.dto.TransferDTO;
import com.monese.task.entity.TransactionEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {

  TransferDTO entityToDto(TransactionEntity transactionEntity);

  List<TransferDTO> entityToDtos(List<TransactionEntity> transactionEntity);
}
