package com.coderman.auth.controller.common;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.user.UserService;
import com.coderman.erp.api.RescApi;
import com.coderman.erp.config.AuthErpConfig;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.redis.RedisService;
import com.coderman.service.dict.ConstItems;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(value = "公共接口", tags = "公共接口")
@RestController
@Slf4j
@RequestMapping(value = "/${domain}")
public class AuthController {

    @Resource
    private RedisService redisService;

    @Resource
    private RescApi rescApi;

    @Resource
    private UserService userService;

    @Resource
    private AuthErpConfig authErpConfig;


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "获取资源信息")
    @PostMapping(value = "/api/resc/all")
    public ResultVO<Map<String, Set<Integer>>> sysRescAll(@RequestParam(value = "domain") String project,
                                                                                                                                     @RequestParam(value = CommonConstant.AUTH_SECURITY_NAME,required = false) String code) {

        this.checkSecurityCode(code);

        return this.rescApi.getSystemAllRescMap(project);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "获取用户信息")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO", value = {"realName", "rescIdList", "deptCode", "username", "token","expiredTime", "deptName", "userId"}),
    })
    @PostMapping(value = "/api/user/info")
    public ResultVO<AuthUserVO> info(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME) String token,
                                                                                      @RequestHeader(value = CommonConstant.AUTH_SECURITY_NAME,required = false) String code) {

        this.checkSecurityCode(code);

        return this.userService.getUserByToken(token);
    }

    private void checkSecurityCode(String code) {
        String securityCode = authErpConfig.getAuthSecurityCode();
        if (StringUtils.isBlank(securityCode)) {
            return;
        }
        if (StringUtils.isNotBlank(securityCode) && !StringUtils.equals(code, securityCode)) {
            throw new BusinessException("权限安全码错误，请业务系统核对！code:" + code);
        }
    }

    /**
     * 系统常量获取
     *
     * @return
     */
    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "常量列表")
    @GetMapping(value = "/const/all")
    @SuppressWarnings("all")
    public ResultVO<Map<String, List<ConstItems>>> constAll() {

        Map<String, List<ConstItems>> resultMap = new HashMap<>();


        this.redisService.getRedisTemplate().execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                StringRedisSerializer s1 = new StringRedisSerializer();
                GenericJackson2JsonRedisSerializer s2 = new GenericJackson2JsonRedisSerializer();

                connection.select(RedisDbConstant.REDIS_DB_DEFAULT);

                byte[] hashKey = s1.serialize("auth.const.all");

                Set<byte[]> fields = connection.hKeys(hashKey);

                for (byte[] field : fields) {

                    String project = new String(field);
                    List<ConstItems> constItems = (List<ConstItems>) s2.deserialize(connection.hGet(hashKey, field));

                    resultMap.put(project, constItems);
                }

                return null;
            }
        });

        ResultVO<Map<String, List<ConstItems>>> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_200);
        resultVO.setResult(resultMap);
        return resultVO;
    }


}
