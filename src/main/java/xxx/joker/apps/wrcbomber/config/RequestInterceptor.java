package xxx.joker.apps.wrcbomber.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xxx.joker.libs.core.datetime.JkDuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static xxx.joker.libs.core.util.JkStrings.strf;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("BEFORE {}", reqToStr(request));
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long elapsedTime = System.currentTimeMillis() - startTime;
        String strElapsed = JkDuration.strElapsed(elapsedTime);
        logger.info("AFTER {}: elapsed time: {}", reqToStr(request), strElapsed);
    }

    private String reqToStr(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        if(StringUtils.isNotBlank(request.getQueryString())) {
            url += "?" + request.getQueryString();
        }
        return strf("request [{}, {}]", request.getMethod(), url);
    }

}