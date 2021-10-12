package com.dm.common.validation.groups;

/**
 * 这个分组表示对某个对象进行引用时，进行验证的分组<br>
 * <p>
 * 示例:
 *
 * <pre>
 * public class User {
 *     private String id;
 *     private String name;
 *     private Title title;
 * }
 *
 * public class Title {
 *     private String id;
 *     private String name;
 * }
 * </pre>
 * <p>
 * 在User中，当我么接收 title作为传入对象时，需要验证id不能为空，<br>
 * <p>
 * 但在单独接受title时，需要name不能为空，这时就需要将进行不同的分组校验
 *
 * @author LiDong
 */
public interface ReferenceGroup {

}
