package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraProfileSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraAudienceDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFeedbackDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara profile dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraProfileDAOImpl implements TotaraProfileDAO {

    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    @Override
    public TotaraUserDTO getUserProfileByEmail(String email) {
        return totaraJdbcTemplate.queryForObject(TotaraProfileSQL.S_SELECT_USER_PROFILE_BY_EMAIL, new Object[] { email },
                new JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
    }

    @Override
    public TotaraUserDTO getUserProfile(Long totaraUserId) {
         return totaraJdbcTemplate.queryForObject(TotaraProfileSQL.S_SELECT_USER_PROFILE, new Object[]{totaraUserId}, new
         JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
    }

    @Override
    public List<TotaraUserDTO> getUserManagers(Long totaraUserId) {
        return totaraJdbcTemplate.query(TotaraProfileSQL.S_SELECT_USER_MANGER, new Object[] { totaraUserId },
                new JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
    }

    @Override
    public List<TotaraAudienceDTO> getUserAudiences(Long totaraUserId) {
        return totaraJdbcTemplate.query(TotaraProfileSQL.S_SELECT_USER_AUDIENCE, new Object[] { totaraUserId },
                new JodaBeanPropertyRowMapper<>(TotaraAudienceDTO.class));
    }

    @Override
    public List<TotaraTagDTO> getUserInterests(Long totaraUserId) {
        return totaraJdbcTemplate.query(TotaraProfileSQL.S_SELECT_USER_TAGS, new Object[] { totaraUserId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> getUserRoles(Long totaraUserId) {
        return totaraJdbcTemplate.query(TotaraProfileSQL.S_SELECT_USER_TAGS_ROLES, new Object[] { totaraUserId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public TotaraFeedbackDTO getUserProfileFieldForFeedback(Long totaraUserId) {
        return totaraJdbcTemplate.queryForObject(TotaraProfileSQL.S_SELECT_USER_PROFILE_FOR_FEEDBACK, new Object[] { totaraUserId },
                new JodaBeanPropertyRowMapper<>(TotaraFeedbackDTO.class));
    }
}
