package com.moon.util.compute.core;

import com.moon.util.DateUtil;
import com.moon.util.ListUtil;
import com.moon.util.MapUtil;
import com.moon.util.compute.RunnerFunction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 内置自定义函数
 *
 * @author benshaoye
 */
final class Inners {

    private final static Map<String, RunnerFunction> CACHE = new HashMap<>();

    static {
        for (NowFunctions value : NowFunctions.values()) {
            CACHE.put(value.name().replace('_', '.'), value);
        }

        for (StrFunctions value : StrFunctions.values()) {
            CACHE.put(value.name().replace('_', '.'), value);
        }

        for (MapFunctions value : MapFunctions.values()) {
            CACHE.put(value.name().replace('_', '.'), value);
        }

        for (ListFunctions value : ListFunctions.values()) {
            CACHE.put(value.name().replace('_', '.'), value);
        }

        for (MathFunctions value : MathFunctions.values()) {
            CACHE.put(value.name().replace('_', '.'), value);
        }

        for (DateFunctions value : DateFunctions.values()) {
            CACHE.put(value.name().replace('_', '.'), value);
        }
    }

    private enum DateFunctions implements RunnerFunction {
        date_format {
            @Override
            public Object execute(Object date, Object pattern) {
                String format = String.valueOf(pattern);
                if (date instanceof Date) {
                    return DateUtil.format((Date) date, format);
                } else if (date instanceof Calendar) {
                    return DateUtil.format((Calendar) date, format);
                }
                throw new IllegalArgumentException(String.valueOf(date));
            }
        },
        date {
            @Override
            public boolean isChangeless() {
                return false;
            }

            @Override
            public Object execute() {
                return new Date();
            }

            @Override
            public Object execute(Object o) {
                return this.execute();
            }

            @Override
            public Object execute(Object o, Object o1) {
                return this.execute();
            }

            @Override
            public Object execute(Object o, Object o1, Object o2) {
                return this.execute();
            }

            @Override
            public Object execute(Object... values) {
                return this.execute();
            }
        };

        @Override
        public String functionName() {
            return this.name().replace('_', '.');
        }
    }

    private enum StrFunctions implements RunnerFunction {
        str_substring {
            @Override
            public String execute(Object str, Object from) {
                return ((String) str).substring(toInt(from));
            }

            @Override
            public String execute(Object str, Object from, Object to) {
                return ((String) str).substring(toInt(from), toInt(to));
            }
        },
        str_contains {
            @Override
            public Object execute(Object value1, Object value2) {
                return String.valueOf(value1).contains(String.valueOf(value2));
            }
        },
        str_indexOf {
            @Override
            public Integer execute(Object value1, Object value2) {
                return String.valueOf(value1).indexOf(String.valueOf(value2));
            }
        },
        str_startsWith {
            @Override
            public Boolean execute(Object value1, Object value2) {
                return String.valueOf(value1).startsWith(String.valueOf(value2));
            }
        },
        str_endsWith {
            @Override
            public Boolean execute(Object value1, Object value2) {
                return String.valueOf(value1).endsWith(String.valueOf(value2));
            }
        },
        str_length {
            @Override
            public Integer execute(Object value) {
                return value == null ? 0 : ((CharSequence) value).length();
            }
        },
        str {
            @Override
            public String execute(Object value) {
                return String.valueOf(value);
            }
        },;

        @Override
        public String functionName() {
            return this.name().replace('_', '.');
        }
    }

    private enum NowFunctions implements RunnerFunction {
        now_year {
            @Override
            public Object execute() {
                return LocalDate.now().getYear();
            }
        },
        now_month {
            @Override
            public Object execute() {
                return LocalDate.now().getMonthValue();
            }
        },
        now_day {
            @Override
            public Object execute() {
                return LocalDate.now().getDayOfMonth();
            }
        },
        now_hour {
            @Override
            public Object execute() {
                return LocalTime.now().getHour();
            }
        },
        now_minute {
            @Override
            public Object execute() {
                return LocalTime.now().getMinute();
            }
        },
        now_second {
            @Override
            public Object execute() {
                return LocalTime.now().getSecond();
            }
        },
        now {
            @Override
            public Object execute() {
                return System.currentTimeMillis();
            }

            @Override
            public Object execute(Object o) {
                return this.execute();
            }

            @Override
            public Object execute(Object o, Object o1) {
                return this.execute();
            }

            @Override
            public Object execute(Object o, Object o1, Object o2) {
                return this.execute();
            }

            @Override
            public Object execute(Object... values) {
                return this.execute();
            }
        };

        @Override
        public String functionName() {
            return this.name().replace('_', '.');
        }

        @Override
        public boolean isChangeless() {
            return false;
        }
    }

    private interface ChangeableRunnerFunction extends RunnerFunction {
        /**
         * 这个函数执行相同参数的返回值是否相同
         *
         * @return
         */
        @Override
        default boolean isChangeless() {
            return false;
        }
    }

    private enum MapFunctions implements ChangeableRunnerFunction {
        map_hasKey {
            @Override
            public Object execute(Object value1, Object value2) {
                return value1 == null ? false : ((Map) value1).containsKey(value2);
            }
        },
        map_hasValue {
            @Override
            public Object execute(Object value1, Object value2) {
                return value1 == null ? false : ((Map) value1).containsValue(value2);
            }
        },
        map_isEmpty {
            @Override
            public Object execute(Object value) {
                return MapUtil.sizeByObject(value) == 0;
            }
        },
        map_get {
            @Override
            public Object execute(Object value1, Object value2) {
                return ListUtil.getByObject(value1, toInt(value2));
            }
        },
        map_size {
            @Override
            public Object execute(Object value) {
                return MapUtil.sizeByObject(value);
            }
        },
        map {
            @Override
            public Object execute() {
                return new HashMap<>();
            }

            @Override
            public Map execute(Object key) {
                HashMap map = new HashMap();
                map.put(formatToKey(key), null);
                return map;
            }

            @Override
            public Map execute(Object key, Object value) {
                HashMap map = new HashMap();
                map.put(formatToKey(key), value);
                return map;
            }

            @Override
            public Map execute(Object key, Object value, Object key1) {
                HashMap map = new HashMap();
                map.put(formatToKey(key), value);
                map.put(formatToKey(key1), null);
                return map;
            }

            @Override
            public Map execute(Object... values) {
                HashMap map = new HashMap();
                int length = values.length;
                if (length > 0) {
                    int mod = length % 2, len = mod == 0 ? length : length - 1;
                    for (int i = 0; i < len; ) {
                        map.put(formatToKey(values[i++]), values[i++]);
                    }
                    if (mod == 0) {
                        map.put(formatToKey(values[len]), null);
                    }
                }
                return map;
            }
        };

        @Override
        public String functionName() {
            return this.name().replace('_', '.');
        }
    }

    private enum ListFunctions implements ChangeableRunnerFunction {
        list_hasIndex {
            @Override
            public Boolean execute(Object value1, Object value2) {
                int index = toInt(value2);
                return value1 == null ? false : index >= 0 && index < ((List) value1).size();
            }
        },
        list_hasValue {
            @Override
            public Boolean execute(Object value1, Object value2) {
                return value1 == null ? false : ((List) value1).contains(value2);
            }
        },
        list_isEmpty {
            @Override
            public Boolean execute(Object value) {
                return ListUtil.isEmpty((List) value);
            }
        },
        list_get {
            @Override
            public Object execute(Object value1, Object value2) {
                return ListUtil.getByObject(value1, toInt(value2));
            }
        },
        list_size {
            @Override
            public Integer execute(Object value) {
                return ListUtil.sizeByObject(value);
            }
        },
        list {
            @Override
            public boolean isChangeless() {
                return true;
            }

            @Override
            public List execute(Object value) {
                return ListUtil.ofArrayList(value);
            }

            @Override
            public List execute(Object value1, Object value2) {
                return ListUtil.ofArrayList(value1, value2);
            }

            @Override
            public List execute(Object value1, Object value2, Object value3) {
                return ListUtil.ofArrayList(value1, value2, value3);
            }

            @Override
            public List execute(Object... values) {
                return ListUtil.ofArrayList(values);
            }
        };

        @Override
        public String functionName() {
            return this.name().replace('_', '.');
        }
    }

    private enum MathFunctions implements RunnerFunction {
        math_ceil {
            @Override
            public Double execute(Object value) {
                return Math.ceil(toDb(value));
            }
        },
        math_floor {
            @Override
            public Double execute(Object value) {
                return Math.floor(toDb(value));
            }
        },
        math_cos {
            @Override
            public Double execute(Object value) {
                return Math.cos(toDb(value));
            }
        },
        math_sin {
            @Override
            public Double execute(Object value) {
                return Math.sin(toDb(value));
            }
        },
        math_tan {
            @Override
            public Double execute(Object value) {
                return Math.tan(toDb(value));
            }
        },
        math_abs {
            @Override
            public Double execute(Object value) {
                return Math.abs(toDb(value));
            }
        },
        math_round {
            @Override
            public Long execute(Object value) {
                return Math.round(toDb(value));
            }
        },
        math_pow {
            @Override
            public Double execute(Object value1, Object value2) {
                return Math.pow(toDb(value1), toDb(value2));
            }
        },
        math_cbrt {
            @Override
            public Double execute(Object value) {
                return Math.cbrt(toDb(value));
            }
        },
        math_sqrt {
            @Override
            public Double execute(Object value) {
                return Math.sqrt(toDb(value));
            }
        },
        math_log {
            @Override
            public Double execute(Object value) {
                return Math.log(toDb(value));
            }
        },
        math_log10 {
            @Override
            public Double execute(Object value) {
                return Math.log10(toDb(value));
            }
        },
        math_random {
            private final ThreadLocalRandom random = ThreadLocalRandom.current();

            @Override
            public boolean isChangeless() {
                return false;
            }

            @Override
            public Double execute() {
                return random.nextDouble();
            }

            @Override
            public Integer execute(Object value) {
                return random.nextInt(toInt(value));
            }

            @Override
            public Integer execute(Object value1, Object value2) {
                return random.nextInt(toInt(value1), toInt(value2));
            }

            @Override
            public Double execute(Object... values) {
                return random.nextDouble();
            }
        };

        @Override
        public String functionName() {
            return this.name().replace('_', '.');
        }
    }

    private final static int toInt(Object value) {
        return value == null ? 0 : ((Number) value).intValue();
    }

    private final static double toDb(Object value) {
        return value == null ? 0 : ((Number) value).doubleValue();
    }

    private final static Object formatToKey(Object key) {
        boolean test = key == null || key instanceof Boolean
            || key instanceof Integer || key instanceof Double;
        if (!test) {
            if (key instanceof Number) {
                key = key instanceof Float ? ((Float) key).doubleValue() : ((Number) key).intValue();
            } else {
                key = String.valueOf(key);
            }
        }
        return key;
    }

    private static void assertVars(String str, int len) {
        for (int i = 0; i < len; i++) {
            if (!ParseUtil.isVar(str.charAt(i))) {
                throw new IllegalArgumentException("包含非法字符（只能是字母、数字、$、_ 的组合）>>>>>" + str + "<<<<<");
            }
        }
    }

    final static String checkName(String name) {
        if (name == null) {
            throw new NullPointerException("RunnerFunction 的 name 不能为空");
        }
        name = name.trim();
        int len = name.length(), curr;
        boolean hasDot = false;
        for (int i = 0; i < len; i++) {
            curr = name.charAt(i);
            if (curr == Constants.DOT && !hasDot) {
                hasDot = true;
            } else if (!ParseUtil.isVar(curr)) {
                throw new IllegalArgumentException("包含非法字符（只能是字母、数字、$、_ 的组合，中间最多可有一个‘.’号以区分命名空间）>>>>>" + name + "<<<<<");
            }
        }
        return name;
    }

    final static String toName(String ns, String name) {
        assertVars((ns = ns.trim()), ns.length());
        assertVars(name, name == null ? 0 : (name = name.trim()).length());
        return ns + (name == null || name.isEmpty() ? name : '.' + name);
    }

    final static RunnerFunction tryLoad(String name) {
        return CACHE.get(name);
    }
}
