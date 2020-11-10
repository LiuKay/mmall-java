//package com.kay.controller.common.interceptors;
//
//import com.kay.common.Const;
//import com.kay.common.ServerResponse;
//import com.kay.pojo.User;
//import com.kay.util.CookieUtil;
//import com.kay.util.JsonUtil;
//import com.kay.util.RedisShardedPoolUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//
///**
// * Created by kay on 2018/5/22.
// * 权限拦截
// */
//@Slf4j
//public class AuthorityInterceptor implements HandlerInterceptor{
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
//        log.info("preHandle");
//        HandlerMethod handlerMethod = (HandlerMethod) o;
//        String methodName = handlerMethod.getMethod().getName();
//        String className = handlerMethod.getBean().getClass().getSimpleName();
//
//        //request中的请求键值对
//        StringBuilder requestParameterString = new StringBuilder();
//
//        Map parameterMap = request.getParameterMap();
//        Iterator iterator = parameterMap.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry entry = (Map.Entry) iterator.next();
//            String mapKey = (String) entry.getKey();
//            Object mapValue = entry.getValue();
//            String mapValueStr = "";
//            //request 的valuew为String[] 对象，需要进行转换
//            if (mapValue instanceof String[]) {
//                String[] strings = (String[]) mapValue;
//                mapValueStr = Arrays.toString(strings);
//            }
//            //拼接请求参数
//            requestParameterString.append(mapKey).append("=").append(mapValueStr);
//        }
//
//        //对于登录请求，一是不能记录敏感信息，二是要返回true进行放行
//        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")) {
//            log.info("权限拦截器拦截请求，className:{},methodName:{}",className,methodName);
//            return true;
//        }
//
//        log.info("权限拦截器拦截请求，className:{},methodName:{},requestParameter:{}",className,methodName,requestParameterString);
//
//
//        //权限验证
//        User user = null;
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isNotEmpty(loginToken)) {
//            user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        }
//        //todo 用户为空或者不是管理员，则需要，重写response,返回不同的结果
//        if (user == null || (user.getRole().intValue() != Const.ROLE.MANAGE_USER)) {
//            //todo 重要--过滤器接管 response，需要重置编码和请求头等
//            response.reset();
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json;charset=UTF-8");
//
//            PrintWriter out = response.getWriter();
//            if (user == null) {
//                //根据业务需要定制不同的返回体
//                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextUpload")) {
//                    Map resultMap = new HashMap();
//                    resultMap.put("success", false);
//                    resultMap.put("msg", "过滤器拦截，用户未登录");
//                    out.write(JsonUtil.obj2string(resultMap));
//                }else {
//                    out.write(JsonUtil.obj2string(ServerResponse.createByErrorMessage("过滤器拦截，用户未登录")));
//                }
//            } else {
//                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextUpload")) {
//                    Map resultMap = new HashMap();
//                    resultMap.put("success", false);
//                    resultMap.put("msg", "过滤器拦截，无操作权限");
//                    out.write(JsonUtil.obj2string(resultMap));
//                }else{
//                    out.write(JsonUtil.obj2string(ServerResponse.createByErrorMessage("过滤器拦截，用户没有权限")));
//                }
//            }
//
//            out.flush();
//            out.close();
//
//            //不放行到controller里面
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//
//    }
//}
