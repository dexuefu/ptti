package cn.ptti.portal.utils;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

/**
 * ZIP对象回调接口
 *
 */
public interface ZipCallback {
	
	void process(ZipArchiveEntry zipEntry) throws Exception;
}
