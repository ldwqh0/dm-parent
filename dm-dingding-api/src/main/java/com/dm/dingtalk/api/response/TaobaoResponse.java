package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.Map;

/**
 * TOPAPI基础响应信息。
 * 
 * @author fengsheng
 */
public abstract class TaobaoResponse implements Serializable {

	private static final long serialVersionUID = 5014379068811962022L;

	private String code;

	/**
	 * errmsg
	 */
	private String errmsg;

	private String message;

	private String errorCode;

	private String msg;

	private String subCode;

	private String subMsg;

	private String subMessage;

	private String flag;

	private String requestId;

	private String qimenType;

	private String body; // API响应JSON或XML串

	private Map<String, String> headerContent; // 响应头

	/**
	 * API请求URL(不包含body)
	 */
	private String requestUrl;

	private Map<String, String> params; // API请求参数列表

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSubCode() {
		return this.subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubMsg() {
		return this.subMsg;
	}

	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isSuccess() {
		return (this.errorCode == null || this.errorCode.isEmpty() || this.errorCode.equals("0"))
				&& (this.subCode == null || this.subCode.isEmpty())
				&& (this.flag == null || this.flag.isEmpty());
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getQimenType() {
		return qimenType;
	}

	public void setQimenType(String qimenType) {
		this.qimenType = qimenType;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Map<String, String> getHeaderContent() {
		return headerContent;
	}

	public void setHeaderContent(Map<String, String> headerContent) {
		this.headerContent = headerContent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubMessage() {
		return subMessage;
	}

	public void setSubMessage(String subMessage) {
		this.subMessage = subMessage;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrmsg() {
		return this.errmsg;
	}
}
