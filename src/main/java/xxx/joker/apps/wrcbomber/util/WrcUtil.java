package xxx.joker.apps.wrcbomber.util;

import static xxx.joker.libs.core.util.JkStrings.strf;

public class WrcUtil {

    public static String signedNum(int num) {
        return strf("{}{}", num > 0 ? "+" : "" , num);
    }
}
