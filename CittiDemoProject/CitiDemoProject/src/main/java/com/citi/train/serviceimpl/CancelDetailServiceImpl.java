package com.citi.train.serviceimpl;

import com.citi.train.dtos.CancelTicketDto;
import com.citi.train.entity.CanceledTicket;
import com.citi.train.enums.TicketStatus;
import com.citi.train.exception.ErrorCodes;
import com.citi.train.exception.SQLExceptions;
import com.citi.train.exception.ServiceException;
import com.citi.train.repository.CancelTicketRepo;
import com.citi.train.repository.TicketDetailsRepo;
import com.citi.train.service.CancelDetailService;
import com.citi.train.uitils.CommonUtils;
import com.citi.train.uitils.DateUtils;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CancelDetailServiceImpl implements CancelDetailService {
    private static final Logger logger = LoggerFactory.getLogger(CancelDetailServiceImpl.class);

    @Autowired
    CancelTicketRepo repo;
    @Autowired
    TicketDetailsRepo ticketDetailsRepo;
    @Autowired
    ModelMapper modelMapper;


    /***
     *
     * @param userid
     * @param fromDate
     * @param toDate
     * @return
     * @throws ServiceException
     * @apiNote this API is used to fetch all canceled ticket from canceledtable
     */

    @Override
    public List<CancelTicketDto>  fetchAllCancelTicket(Long userid,String fromDate,String toDate) throws ServiceException {
        try {
            logger.info("FetchAllCancelTicket Service Called|");
            LocalDateTime from = DateUtils.stringToDateFormat(fromDate);
            LocalDateTime to = DateUtils.stringToDateFormat(toDate);
            logger.info("MyBookingTickets Service Called|{}|{}", from, to);
            Optional<List<CanceledTicket>> ticketEntity = repo.getCancelTickets(userid,from,to);
            List<CancelTicketDto> responseDto = new ArrayList<>();
            if (ticketEntity.isPresent() && !ticketEntity.get().isEmpty()) {
                for (CanceledTicket ticket : ticketEntity.get()) {
                    CancelTicketDto response = modelMapper.map(ticket,CancelTicketDto.class);
                    modelMapper.map(ticket.getTicketId(),response);
                    modelMapper.map(ticket.getTicketId().getTrianId(),response);
                    responseDto.add(response);
                }
                return responseDto;
            } else {
                logger.error("MyBookingTickets|{}|{}|{}", fromDate, toDate, ErrorCodes.NO_CANCELED_TICKET);
                throw new ServiceException(ErrorCodes.NO_CANCELED_TICKET);
            }
        } catch (DataAccessException | MappingException he) {
            logger.error("ErroMessage",he.fillInStackTrace());
            throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }

    /**
     *
     * @param dto
     * @return
     * @throws ServiceException
     * @throws SQLExceptions
     * @apiNote this API is used to cancel the particular tickets
     */
    @Override
    public CancelTicketDto cancelTicket(CancelTicketDto dto) throws ServiceException,SQLExceptions {
        try {
            dto.setCancelDateTime(DateUtils.currentDateTimeFormatInDateFormat());
            dto.setRefundStatus("INPROGRESS");
            CanceledTicket map = CommonUtils.getMapper(dto, CanceledTicket.class);
            CanceledTicket saved = repo.save(map);
            CancelTicketDto dtoReturn = CommonUtils.getMapper(saved, CancelTicketDto.class);
            if (!CommonUtils.checkNullable(dtoReturn)) {
                ticketDetailsRepo.ticketStatus(String.valueOf(TicketStatus.CANCALLED), DateUtils.currentDateTimeFormatInString(), dto.getTicketid());
            }
            return dtoReturn;
        }catch  (DataAccessException|MappingException he) {
            logger.error("ErroMessage",he.fillInStackTrace());
            throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }

}
