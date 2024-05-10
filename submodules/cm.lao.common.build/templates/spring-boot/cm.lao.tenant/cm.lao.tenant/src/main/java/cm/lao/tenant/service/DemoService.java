package cm.lao.tenant.service;

import cm.lao.tenant.dto.DemoDTO;
import cm.lao.tenant.repository.DemoRepository;
import cm.lao.tenant.service.mapper.DemoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DemoService {

    private final DemoRepository demoRepository;
    private final DemoMapper demoMapper;

    public List<DemoDTO> fetchAll(String fieldsExtractionCode) {
        return demoRepository
                .findAll()
                .stream()
                .map(demo -> demoMapper.toDto(demo, fieldsExtractionCode))
                .toList();
    }
}
