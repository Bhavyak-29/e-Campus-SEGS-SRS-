package com.ec2.main.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.ec2.main.model.Role;
import com.ec2.main.model.Users;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Users> userRowMapper = (rs, rowNum) -> {
        Users user = new Users();
        user.setUid(rs.getLong("uid"));
        user.setUnivId(rs.getString("univid"));
        user.setUname(rs.getString("uname"));
        user.setUtype(rs.getString("utype"));

        Role role = new Role();
        role.setRid(rs.getString("role_id"));  // Make sure  query aliases urole as role_id
        user.setRole(role);

        user.setUemail(rs.getString("uemail"));
        user.setCreatedBy(rs.getLong("created_by"));
        user.setCreateTs(rs.getObject("creat_ts", LocalDateTime.class));
        user.setLastUpdatedBy(rs.getLong("last_updated_by"));
        user.setLastUpdatedTs(rs.getObject("last_updated_ts", LocalDateTime.class));
        user.setStatus(rs.getBoolean("status"));
        return user;
    };

    public Users getUserDetails(int userId) {
        String sql = """
            SELECT u.*, r.rid AS role_id FROM users u
            LEFT JOIN ec2_roles r ON u.urole = r.rid
            WHERE u.uid = ? AND u.status = true
        """;

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserCategoryFromRoleName(Users user) {
        if (user.getRole() == null || user.getRole().getRoleName() == null) {
            return "UNKNOWN";
        }
        return user.getRole().getRoleName();
    }

}
