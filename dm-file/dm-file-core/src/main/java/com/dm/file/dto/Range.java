package com.dm.file.dto;

import com.dm.file.exception.RangeNotSatisfiableException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 标识一个文件下载范围请求的范围
 *
 * @author ldwqh0@outlook.con
 * @since 0.2.3
 */

public interface Range {

    long getStart();

    long getEnd();

    long getContentLength();

    static Range of(String str, long count) throws RangeNotSatisfiableException {
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

    static List<Range> of(List<String> strs, long count) throws RangeNotSatisfiableException {
        return of(strs.toArray(new String[]{}), count);
    }

    static List<Range> of(String[] strs, long count) throws RangeNotSatisfiableException {
        List<Range> result = new ArrayList<>();
        for (String range : strs) {
            result.add(Range.of(StringUtils.trim(range), count));
        }
        return result;
    }

    class RangeImpl implements Range {
        private long start;
        private long end;

        @Override
        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        @Override
        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        @Override
        public long getContentLength() {
            return end - start + 1;
        }

        public RangeImpl(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RangeImpl range = (RangeImpl) o;
            return start == range.start && end == range.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

}
