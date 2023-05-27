package com.kodlamaio.invoiceservice.business.concretes;

import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.business.dto.requests.create.CreateInvoiceRequest;
import com.kodlamaio.invoiceservice.business.dto.requests.update.UpdateInvoiceRequest;
import com.kodlamaio.invoiceservice.business.dto.responses.create.CreateInvoiceResponse;
import com.kodlamaio.invoiceservice.business.dto.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceservice.business.dto.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.business.dto.responses.update.UpdateInvoiceResponse;
import com.kodlamaio.invoiceservice.entities.Invoice;
import com.kodlamaio.invoiceservice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InvoiceManager implements InvoiceService {
    private InvoiceRepository repository;
    private ModelMapperService modelMapperService;

    @Autowired
    public InvoiceManager(InvoiceRepository repository) {
        this.repository = repository;
    }
    public InvoiceManager(ModelMapperService modelMapperService) {
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<GetAllInvoicesResponse> getAll() {
        List<Invoice> invoices = repository.findAll();
        List<GetAllInvoicesResponse> response = invoices
                .stream()
                .map(invoice -> modelMapperService.forResponse().map(invoice, GetAllInvoicesResponse.class))
                .toList();
        return response;
    }

    @Override
    public GetInvoiceResponse getById(String id) {
        checkIfInvoiceExists(id);
        Invoice invoice = repository.findById(id).orElseThrow();
        GetInvoiceResponse response = modelMapperService.forRequest().map(invoice, GetInvoiceResponse.class);
        return response;
    }

    @Override
    public CreateInvoiceResponse add(CreateInvoiceRequest request) {
        Invoice invoice = modelMapperService.forRequest().map(request, Invoice.class);
        invoice.setId(null);
        invoice.setTotalPrice(getTotalPrice(invoice));
        repository.save(invoice);
        CreateInvoiceResponse response = modelMapperService.forResponse().map(invoice, CreateInvoiceResponse.class);
        return response;
    }

    @Override
    public Invoice create(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public UpdateInvoiceResponse update(String id, UpdateInvoiceRequest request) {
        checkIfInvoiceExists(id);
        Invoice invoice = modelMapperService.forRequest().map(request, Invoice.class);
        invoice.setId(null);
        invoice.setTotalPrice(getTotalPrice(invoice));
        repository.save(invoice);
        UpdateInvoiceResponse response = modelMapperService.forResponse().map(invoice, UpdateInvoiceResponse.class);
        return response;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);

    }

    private double getTotalPrice(Invoice invoice) {
        return invoice.getDailyPrice() * invoice.getRentedForDays();
    }

    private void checkIfInvoiceExists(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("fatura bilgisi bulunamadı");
        }
    }
}
