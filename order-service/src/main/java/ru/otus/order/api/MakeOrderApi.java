package ru.otus.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import ru.otus.order.api.dto.MakeOrderRequest;

import java.util.Optional;

@Validated
@Tag(name = "makeOrder", description = "the makeOrder API")
@RequestMapping(path = "/order-service")
public interface MakeOrderApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /makeOrder : IF4 - Make order
     *
     * @param makeOrderRequest (required)
     * @return Order created (status code 204)
     */
    @Operation(operationId = "makeOrder", summary = "IF4 - Make order", responses = {@ApiResponse(responseCode = "204", description = "Order created")})
    @RequestMapping(method = RequestMethod.POST, value = "/makeOrder", consumes = {"application/json"})

    default ResponseEntity<Void> makeOrder(@Parameter(name = "MakeOrderRequest", description = "", required = true) @Valid @RequestBody MakeOrderRequest makeOrderRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
