package jp.co.hogehoge.framework.property;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * 1次キャッシュ定義
 *
 * @param <T> プロパティ値のデータ型
 */
public abstract class PrimaryCache<T> {

	/**
	 * キャッシュ無し定義を作成する。
	 * 
	 * @param <T> プロパティ値のデータ型
	 * @return 1次キャッシュ
	 */
	public static <T> PrimaryCache<T> isNone() {
		return new PrimaryCache<T>() {
			@Override
			public Optional<T> get(String fileName, String key, PropertyResource resource, PropertyType<T> parser) {
				return parser.parse(SecondaryCache.NONE.get(fileName, resource), key);
			}
		};
	}

	/**
	 * キャッシュメモリ定義を作成する。
	 * 
	 * @param <T> プロパティ値のデータ型
	 * @return 1次キャッシュ
	 */
	public static <T> PrimaryCache<T> isMemory() {
		return new PrimaryCache<T>() {
			private Optional<T> cache = null;

			@Override
			public Optional<T> get(String fileName, String key, PropertyResource resource, PropertyType<T> parser) {
				if (Objects.isNull(this.cache)) {
					this.cache = parser.parse(SecondaryCache.MEMORY.get(fileName, resource), key);
				}
				return this.cache;
			}
		};
	}

	/**
	 * 更新確認を行うキャッシュメモリ定義を作成する。
	 * 
	 * @param <T> プロパティ値のデータ型
	 * @return 1次キャッシュ
	 */
	public static <T> PrimaryCache<T> isRefreshMemory() {
		return new PrimaryCache<T>() {
			private Optional<T> cache = null;

			@Override
			public Optional<T> get(String fileName, String key, PropertyResource resource, PropertyType<T> parser) {
				Properties p = SecondaryCache.MEMORY_REFRESH.get(fileName, resource);
				if (Objects.nonNull(p)) {
					this.cache = parser.parse(p, key);
				}
				return this.cache;
			}
		};
	}

	/**
	 * プロパティ値を取得する。
	 * 
	 * @param fileName プロパティ・ファイル名
	 * @param key      プロパティ・キー
	 * @param resource プロパティ・リソース
	 * @param type     プロパティ・パーサー
	 * @return プロパティ値
	 */
	public abstract Optional<T> get(String fileName, String key, PropertyResource resource, PropertyType<T> type);

}
