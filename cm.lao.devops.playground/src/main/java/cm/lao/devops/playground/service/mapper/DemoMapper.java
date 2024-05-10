package cm.lao.devops.playground.service.mapper;

import cm.lao.common.microservice.common.service.mapper.UuidMapper;
import cm.lao.common.microservice.common.util.SmartOptional;
import cm.lao.devops.playground.domain.Demo;
import cm.lao.devops.playground.domain.DemoId;
import cm.lao.devops.playground.dto.DemoDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static cm.lao.common.microservice.common.util.Constants.FIELDS_TO_EXTRACT_1;

@Mapper(
        uses = {
                UuidMapper.class,
        },
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DemoMapper {
    default DemoDTO toDto(Demo demo, String fieldsExtractionCode) {
        if (FIELDS_TO_EXTRACT_1.equals(fieldsExtractionCode)) {
            return fieldsToExtract1(demo);
        }
        throw new IllegalArgumentException(
                String.format("Extraction Code '%s' is not supported", fieldsExtractionCode)
        );
    }


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "name")
    DemoDTO fieldsToExtract1(Demo demo);

    default String map(DemoId value) {
        return SmartOptional.ofNullable(value)
                .map(DemoId::getValue)
                .orElse(null);
    }

}
