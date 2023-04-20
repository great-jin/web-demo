package xyz.ibudai.service.Impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.ibudai.dao.AuthUserDao;
import xyz.ibudai.entity.AuthUser;
import xyz.ibudai.service.AuthUserService;
import xyz.ibudai.util.AESUtil;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * (TbUser)表服务实现类
 *
 * @author makejava
 * @since 2023 -01-31 14:31:28
 */
@Service("tbUserService")
public class AuthUserServiceImpl implements AuthUserService {

    @Resource
    private AuthUserDao authUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authUserDao.queryByName(username);
    }

    @Override
    public boolean authVerify(AuthUser user) throws Exception {
        boolean isVerify = false;
        AuthUser dbUser = authUserDao.queryById(user.getId());
        if (dbUser == null) {
            throw new RuntimeException("User [" + user.getId() + "] doesn't exist.");
        }
        String userName = user.getUsername();
        String userPwd = user.getPassword();
        String dbUserName = dbUser.getUsername();
        String dbUserPwd = AESUtil.desEncrypt(dbUser.getPassword()).trim();
        if (Objects.equals(userName, dbUserName)) {
            isVerify = Objects.equals(userPwd, dbUserPwd);
        }
        return isVerify;
    }
}
