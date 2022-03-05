package jp.co.hogehoge.framework.property;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.MissingResourceException;
import java.util.Properties;

import jp.co.hogehoge.framework.charset.CharCode;

/**
 * プロパティ・リソース定義
 */
public enum PropertyResource {

	FILE {

		@Override
		public Properties get(String fileName) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			try (InputStream istream = classLoader.getResourceAsStream(fileName)) {
				Properties properties = new Properties();
				properties.load(new InputStreamReader(istream, CharCode.UTF8.getDecoder()));
				return properties;
			} catch (Exception e) {
				throw new MissingResourceException("プロパティファイルの取得に失敗しました。[ファイル名:" + fileName + "]\n", fileName, fileName);
			}
		}
	},

	SYSTEM {

		@Override
		public Properties get(String fileName) {
			return System.getProperties();
		}
	},

	ENV {

		@Override
		public Properties get(String fileName) {
			Properties prop = new Properties();
			prop.putAll(System.getenv());
			return prop;
		}
	};

	/**
	 * プロパティを取得する。
	 * 
	 * @param fileName プロパティファイル名
	 * @return
	 */
	public abstract Properties get(String fileName);

}
