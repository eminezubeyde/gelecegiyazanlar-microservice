package com.kodlamaio.paymentservice.business.concretes;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.paymentservice.business.abstracts.PaymentService;
import com.kodlamaio.paymentservice.business.abstracts.PosService;
import com.kodlamaio.paymentservice.business.dto.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentservice.business.dto.responses.get.GetPaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.update.UpdatePaymentsResponse;
import com.kodlamaio.paymentservice.business.rules.PaymentBusinessRules;
import com.kodlamaio.paymentservice.entity.Payment;
import com.kodlamaio.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
    private final PaymentRepository repository;
    private final PaymentBusinessRules rules;
    private final PosService posService;
    private final ModelMapperService mapper;


    @Override
    public List<GetAllPaymentsResponse> getAll() {
        var payments = repository.findAll();
        return payments.stream().map(payment -> mapper.forResponse().map(payment, GetAllPaymentsResponse.class)).toList();
    }

    @Override
    public GetPaymentResponse getById(UUID id) {
        var payment = repository.findById(id).orElseThrow();
        return mapper.forResponse().map(payment, GetPaymentResponse.class);
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request) {
        rules.checkIfCardExists(request);
        var payment = mapper.forRequest().map(request, Payment.class);
        payment.setId(null);
        repository.save(payment);

        return mapper.forResponse().map(payment, CreatePaymentResponse.class);
    }

    @Override
    public UpdatePaymentsResponse update(UUID id, UpdatePaymentRequest request) {
        rules.checkIfPaymentExists(id);
        var payment = mapper.forRequest().map(request, Payment.class);
        payment.setId(id);
        repository.save(payment);
        return mapper.forResponse().map(payment, UpdatePaymentsResponse.class);
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfPaymentExists(id);
        repository.deleteById(id);
    }

    @Override
    public ClientResponse processRentalPayment(CreateRentalPaymentRequest request) {
        ClientResponse clientResponse = new ClientResponse();
        var payment = repository.findByCardNumber(request.getCardNumber());
        validateIfPayment(request, clientResponse, payment.getBalance());
        payment.setBalance(payment.getBalance() - request.getPrice());
        repository.save(payment);

        return clientResponse;
    }

    private void validateIfPayment(CreateRentalPaymentRequest request, ClientResponse clientResponse, double balance) {
        try {
            rules.checkIfPaymentIsValid(request);
            rules.checkIfBalanceIsEnough(request.getPrice(), balance);
            posService.pay();
            clientResponse.setSuccess(true);
        } catch (BusinessException exception) {
            clientResponse.setSuccess(false);
            clientResponse.setMessage(exception.getMessage());
        }
    }
}