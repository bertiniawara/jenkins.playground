package cm.lao.devops.playground.api;

import cm.lao.devops.playground.dto.DemoDTO;
import cm.lao.devops.playground.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DemoResources implements DemoApi {

    private final DemoService demoService;

    @Override
    public ResponseEntity<List<DemoDTO>> fetchAllDemo(String fieldsToExtractCode) {
        return ResponseEntity.ok(demoService.fetchAll(fieldsToExtractCode));
    }
}
