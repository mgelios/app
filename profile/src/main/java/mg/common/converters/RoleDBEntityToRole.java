package mg.common.converters;

import mg.common.dbentities.RoleDBEntity;
import mg.common.models.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleDBEntityToRole implements Converter<RoleDBEntity, Role> {

    @Override
    public Role convert(RoleDBEntity source) {
        Role target = new Role();
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }
}
