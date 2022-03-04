package jp.co.hogehoge.framework.property;

/**
 * プロパティ定義。
 * 
 * @param <T> プロパティのデータ型
 */
public abstract class Property<T> {

	/**
	 * クラスパス上のプロパティ・ファイルからプロパティ値を取得するプロパティを定義する。
	 * プロパティはメモリ上にキャッシュされないため、取得する度にファイルの読込みが発生する。
	 * 
	 * @param              <T> プロパティのデータ型
	 * @param fileName     プロパティ・ファイル名
	 * @param key          プロパティ・キー
	 * @param defaultValue プロパティのデフォルト値
	 * @param type         プロパティのデータ型
	 * @return プロパティ
	 */
	public static <T> Property<T> defineNonCache(String fileName, String key, T defaultValue, PropertyType<T> type) {
		return define(fileName, key, defaultValue, type, PrimaryCache.isNone(), PropertyResource.FILE);
	}

	/**
	 * クラスパス上のプロパティ・ファイルからプロパティ値を取得するプロパティを定義する。
	 * プロパティはメモリ上にキャッシュされるため、初回のみファイルの読込が発生する。
	 * 
	 * @param              <T> プロパティのデータ型
	 * @param fileName     プロパティ・ファイル
	 * @param key          プロパティ・キー
	 * @param defaultValue プロパティのデフォルト値
	 * @param type         プロパティのデータ型
	 * @return プロパティ
	 */
	public static <T> Property<T> define(String fileName, String key, T defaultValue, PropertyType<T> type) {
		return define(fileName, key, defaultValue, type, PrimaryCache.isMemory(), PropertyResource.FILE);
	}

	/**
	 * クラスパス上のプロパティ・ファイルから最新のプロパティ値を取得するプロパティを定義する。
	 * プロパティはメモリ上にキャッシュされるが、プロパティ・ファイルの更新を常に確認し、
	 * 更新された場合は、最新のプロパティ・ファイルを取得し、メモリ上にキャッシュした上で、 プロパティ値を取得する。
	 * 
	 * @param              <T> プロパティのデータ型
	 * @param fileName     プロパティ・ファイル
	 * @param key          プロパティ・キー
	 * @param defaultValue プロパティのデフォルト値
	 * @param type         プロパティのデータ型
	 * @return プロパティ
	 */
	public static <T> Property<T> defineUpdateCheck(String fileName, String key, T defaultValue, PropertyType<T> type) {
		return define(fileName, key, defaultValue, type, PrimaryCache.isRefreshMemory(), PropertyResource.FILE);
	}

	/**
	 * システム・プロパティを定義する。
	 * 
	 * @param key          プロパティ・キー
	 * @param defaultValue プロパティのデフォルト値
	 * @param type         プロパティのデータ型
	 * @return プロパティ
	 */
	public static <T> Property<T> defineSystem(String key, T defaultValue, PropertyType<T> type) {
		return define("property\\system", key, defaultValue, type, PrimaryCache.isMemory(), PropertyResource.SYSTEM);
	}

	/**
	 * 環境変数を定義する。
	 * 
	 * @param key          キー
	 * @param defaultValue プロパティのデフォルト値
	 * @param type         変数のデータ型
	 * @return プロパティ
	 */
	public static <T> Property<T> defineEnv(String key, T defaultValue, PropertyType<T> type) {
		return define("property\\env", key, defaultValue, type, PrimaryCache.isMemory(), PropertyResource.ENV);
	}

	/**
	 * プロパティを定義する。
	 * 
	 * @param fileName     プロパティ・ファイル名
	 * @param key          プロパティ・キー
	 * @param defaultValue プロパティのデフォルト値
	 * @param parser       プロパティ・パーサー
	 * @param cache        1次キャッシュ
	 * @param resource     プロパティ・リソース
	 * @return プロパティ
	 */
	private static <T> Property<T> define(String fileName, String key, T defaultValue, PropertyType<T> parser,
			PrimaryCache<T> cache, PropertyResource resource) {
		return new Property<T>() {

			@Override
			public T get() {
				return cache.get(fileName, key, resource, parser).orElse(defaultValue);
			}

			@Override
			public String getKey() {
				return key;
			}

			@Override
			public String getFileName() {
				return fileName;
			};
		};
	}

	/**
	 * プロパティの値を取得する。
	 * 
	 * @return 値
	 */
	public abstract T get();

	/**
	 * プロパティのキーを取得する。
	 * 
	 * @return キー
	 */
	public abstract String getKey();

	/**
	 * 取得元のプロパティ・ファイル名を取得する。
	 * 
	 * @return プロパティ・ファイル名
	 */
	public abstract String getFileName();
}
