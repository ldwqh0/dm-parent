package com.dm.file.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.dm.file.exception.RangeNotSatisfiableException;

import lombok.Data;

/**
 * 标识一个文件下载范围请求的范围
 * 
 * @author ldwqh0@outlook.con
 * @since 0.2.3
 *
 */

public interface Range {

    public long getStart();

    public long getEnd();

    public long getContentLength();

    public static Range of(String str, long count) throws RangeNotSatisfiableException {
        try {
            if (StringUtils.equals(str, "-")) {
                throw new RangeNotSatisfiableException();
            }
            if (str.endsWith("-")) {
                String num = StringUtils.stripEnd(str, "-").trim();
                if (NumberUtils.isCreatable(num)) {
                    return new RangeImpl(NumberUtils.toLong(num), count - 1);
                } else {
                    throw new RangeNotSatisfiableException();
                }
            }
            if (str.startsWith("-")) {
                String num = StringUtils.stripStart(str, "-").trim();
                if (NumberUtils.isCreatable(num)) {
                    long length = NumberUtils.toLong(num);
                    long start = count - length;
                    if (start < 0) {
                        start = 0;
                    }
                    return new RangeImpl(start, count - 1);
                } else {
                    throw new RangeNotSatisfiableException();
                }
            }
            String[] nums = StringUtils.split(str, "-");
            String num0 = StringUtils.trim(nums[0]);
            String num1 = StringUtils.trim(nums[1]);
            if (NumberUtils.isCreatable(num0) && NumberUtils.isCreatable(num1)) {
                long start = NumberUtils.toLong(num0);
                long end = NumberUtils.toLong(num1);
                if (end < start || end > count) {
                    throw new RangeNotSatisfiableException();
                } else {
                    return new RangeImpl(start, end);
                }
            } else {
                throw new RangeNotSatisfiableException();
            }
        } catch (RangeNotSatisfiableException e) {
            throw e;
        } catch (Exception e) {
            throw new RangeNotSatisfiableException();
        }
    }

    public static List<Range> of(List<String> strs, long count) throws RangeNotSatisfiableException {
        return of(strs.toArray(new String[] {}), count);
    }

    public static List<Range> of(String[] strs, long count) throws RangeNotSatisfiableException {
        List<Range> result = new ArrayList<Range>();
        for (String range : strs) {
            result.add(Range.of(StringUtils.trim(range), count));
        }
        return result;
    }

    @Data
    static class RangeImpl implements Range {
        private long start;
        private long end;

        @Override
        public long getContentLength() {
            return end - start + 1;
        }

        public RangeImpl(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

}
