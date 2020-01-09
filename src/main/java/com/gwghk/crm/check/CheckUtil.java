package com.gwghk.crm.check;

import com.gwghk.crm.check.checker.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CheckUtil {
    public static final Map<String, BaseChecker> checkers = Collections.unmodifiableMap(new HashMap<String, BaseChecker>() {{
        put(BaseChecker.key, new BaseChecker());
        put(NoChecker.key, new NoChecker());
        put(DateChecker.key, new DateChecker());
        put(DateMonChecker.key, new DateMonChecker());
    }});
}
