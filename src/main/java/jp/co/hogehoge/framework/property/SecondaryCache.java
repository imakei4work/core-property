package jp.co.hogehoge.framework.property;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2次キャッシュ定義
 */
public enum SecondaryCache {

	NONE {

		@Override
		public Properties get(String fileName, PropertyResource resource) {
			return resource.get(fileName);
		}
	},

	MEMORY {

		/** プロパティファイル情報 */
		private final Map<String, Properties> cache = new ConcurrentHashMap<>();

		@Override
		public Properties get(String fileName, PropertyResource resource) {
			return this.cache.computeIfAbsent(fileName, f -> resource.get(f));
		}
	},

	MEMORY_REFRESH {

		/** 最終更新日時情報 */
		private final Map<String, FileTime> cache = new ConcurrentHashMap<>();

		@Override
		public Properties get(String fileName, PropertyResource resource) {
			try {
				URI uri = Thread.currentThread().getContextClassLoader().getResource(fileName).toURI();
				FileTime fileTime = Files.getLastModifiedTime(Paths.get(uri).toAbsolutePath());
				if (!fileTime.equals(this.cache.get(fileName))) {
					this.cache.put(fileName, fileTime);
					return resource.get(fileName);
				}
				return null;
			} catch (Exception e) {
				throw new MissingResourceException("プロパティファイルの更新日時の取得に失敗しました。[" + fileName + "]\n", fileName, fileName);
			}
		}
	};

	/**
	 * プロパティを取得する。
	 * 
	 * @param fileName プロパティファイル名
	 * @return
	 */
	public abstract Properties get(String fileName, PropertyResource resource);

}
