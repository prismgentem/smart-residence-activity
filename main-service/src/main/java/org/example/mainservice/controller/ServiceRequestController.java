package org.example.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.model.reqest.ServiceRequestV1Request;
import org.example.mainservice.model.response.ServiceRequestV1Response;
import org.example.mainservice.service.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-request")
public class ServiceRequestController {
    private final ServiceRequestService serviceRequestService;
    @PostMapping
    public ResponseEntity<ServiceRequestV1Response> createServiceRequest(@RequestBody ServiceRequestV1Request serviceRequest) {
        return ResponseEntity.ok(serviceRequestService.createServiceRequest(serviceRequest));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestV1Response> getServiceRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(serviceRequestService.getServiceRequestById(id));
    }
    @GetMapping
    public ResponseEntity<List<ServiceRequestV1Response>> getAllServiceRequest() {
        return ResponseEntity.ok(serviceRequestService.getAllServiceRequest());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestV1Response> updateServiceRequestNews(@PathVariable UUID id, @RequestBody ServiceRequestV1Request serviceRequest) {
        return ResponseEntity.ok(serviceRequestService.updateServiceRequest(id, serviceRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequest(@PathVariable UUID id) {
        serviceRequestService.deleteServiceRequest(id);
        return ResponseEntity.noContent().build();
    }
}
