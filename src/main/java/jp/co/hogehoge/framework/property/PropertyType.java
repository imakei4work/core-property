package jp.co.hogehoge.framework.property;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * プロパティ値の非プリミティブ型定義
 * 
 * @param <T> プロパティ値のデータ型
 */
public abstract class PropertyType<T> {

	/** 区切り文字 */
	protected String delimiter = ";";

	/**
	 * プロパティ値をString型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<String> isString() {
		return new PropertyType<String>() {

			@Override
			public Optional<String> parse(Properties properties, String key) {
				String v = properties.getProperty(key, "");
				return Optional.ofNullable(v.isEmpty() ? null : v);
			}

		};
	}

	/**
	 * プロパティ値をInteger型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Integer> isInteger() {
		return new PropertyType<Integer>() {

			@Override
			public Optional<Integer> parse(Properties properties, String key) {
				String v = properties.getProperty(key, "");
				return Optional.ofNullable(v.isEmpty() ? null : Integer.valueOf(v));
			}

		};
	}

	/**
	 * プロパティ値をBoolean型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Boolean> isBoolean() {
		return new PropertyType<Boolean>() {
			@Override
			public Optional<Boolean> parse(Properties properties, String key) {
				String v = properties.getProperty(key, "");
				return Optional.ofNullable(v.isEmpty() ? null : Boolean.valueOf(v));
			}
		};
	}

	/**
	 * プロパティのリストをString型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<List<String>> isStringList() {
		return new PropertyType<List<String>>() {

			@Override
			public Optional<List<String>> parse(Properties properties, String key) {
				String v = properties.getProperty(key, "");
				return Optional.ofNullable(v.isEmpty() ? null : Arrays.asList(v.split(this.delimiter, -1))); // 最後のセミコロンもカウント
			}

		};
	}

	/**
	 * プロパティのリストをInteger型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<List<Integer>> isIntegerList() {
		return new PropertyType<List<Integer>>() {

			@Override
			public Optional<List<Integer>> parse(Properties properties, String key) {
				String v = properties.getProperty(key, "");
				List<Integer> list = v.isEmpty() ? null
						: Arrays.asList(v.split(this.delimiter, -1)) // 最後のセミコロンもカウント
								.stream()
								.map(Integer::valueOf)
								.collect(Collectors.toList());
				return Optional.ofNullable(list);
			}

		};
	}

	/**
	 * プロパティのリストをBoolean型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<List<Boolean>> isBooleanList() {
		return new PropertyType<List<Boolean>>() {

			@Override
			public Optional<List<Boolean>> parse(Properties properties, String key) {
				String v = properties.getProperty(key, "");
				List<Boolean> list = v.isEmpty() ? null
						: Arrays.asList(v.split(this.delimiter, -1)) // 最後のセミコロンもカウント
								.stream()
								.map(Boolean::valueOf)
								.collect(Collectors.toList());
				return Optional.ofNullable(list);
			}

		};
	}

	/**
	 * プロパティのマップ値をString型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Map<String, String>> isStringMap() {
		return new PropertyType<Map<String, String>>() {

			@Override
			public Optional<Map<String, String>> parse(Properties properties, String key) {
				Map<String, String> data = properties.entrySet()
						.parallelStream()
						.filter(e -> e.getKey().toString().startsWith(key))
						.collect(Collectors.toConcurrentMap(e -> e.getKey().toString(),
								e -> e.getValue().toString(),
								(a, b) -> b));// 重複キーがあれば後勝ち
				return Optional.ofNullable(data.isEmpty() ? null : data);
			}
		};
	}

	/**
	 * プロパティのマップ値をInteger型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Map<String, Integer>> isIntegerMap() {
		return new PropertyType<Map<String, Integer>>() {

			@Override
			public Optional<Map<String, Integer>> parse(Properties properties, String key) {
				Map<String, Integer> data = properties.entrySet()
						.parallelStream()
						.filter(e -> e.getKey().toString().startsWith(key))
						.collect(Collectors.toConcurrentMap(e -> e.getKey().toString(),
								e -> Integer.valueOf(e.getValue().toString()),
								(a, b) -> b)); // 重複キーがあれば後勝ち
				return Optional.ofNullable(data.isEmpty() ? null : data);
			}
		};
	}

	/**
	 * プロパティのマップ値をBoolean型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Map<String, Boolean>> isBooleanMap() {
		return new PropertyType<Map<String, Boolean>>() {

			@Override
			public Optional<Map<String, Boolean>> parse(Properties properties, String key) {
				Map<String, Boolean> data = properties.entrySet()
						.parallelStream()
						.filter(e -> e.getKey().toString().startsWith(key))
						.collect(Collectors.toConcurrentMap(e -> e.getKey().toString(),
								e -> Boolean.valueOf(e.getValue().toString()),
								(a, b) -> b)); // 重複キーがあれば後勝ち
				return Optional.ofNullable(data.isEmpty() ? null : data);
			}
		};
	}

	/**
	 * プロパティのマップ値をリストのString型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Map<String, List<String>>> isStringMapList() {
		return new PropertyType<Map<String, List<String>>>() {

			@Override
			public Optional<Map<String, List<String>>> parse(Properties properties, String key) {
				Map<String, List<String>> data = properties.entrySet()
						.parallelStream()
						.filter(e -> e.getKey().toString().startsWith(key))
						.collect(Collectors.toConcurrentMap(e -> e.getKey().toString(),
								e -> e.getValue().toString().isEmpty() ? Collections.emptyList()
										: Arrays.asList(e.getValue().toString().split(this.delimiter, -1)),
								(a, b) -> b)); // 重複キーがあれば後勝ち
				return Optional.ofNullable(data.isEmpty() ? null : data);
			}

		};
	}

	/**
	 * プロパティのマップ値をリストのInteger型で定義する。
	 * 
	 * @return プロパティ型
	 */
	public static PropertyType<Map<String, List<Integer>>> isIntegerMapList() {
		return new PropertyType<Map<String, List<Integer>>>() {

			@Override
			public Optional<Map<String, List<Integer>>> parse(Properties properties, String key) {
				Map<String, List<Integer>> data = properties.entrySet()
						.parallelStream()
						.filter(e -> e.getKey().toString().startsWith(key))
						.collect(Collectors.toConcurrentMap(e -> e.getKey().toString(),
								e -> e.getValue().toString().isEmpty() ? Collections.emptyList()
										: Arrays.asList(e.getValue().toString().split(this.delimiter, -1))
												.stream()
												.map(Integer::valueOf)
												.collect(Collectors.toList()),
								(a, b) -> b));// 重複キーがあれば後勝ち
				return Optional.ofNullable(data.isEmpty() ? null : data);
			}
		};
	}

	/**
	 * プロパティのマップ値をリストのBoolean型で定義する。
	 * 
	 * @return
	 */
	public static PropertyType<Map<String, List<Boolean>>> isBooleanMapList() {
		return new PropertyType<Map<String, List<Boolean>>>() {

			@Override
			public Optional<Map<String, List<Boolean>>> parse(Properties properties, String key) {
				Map<String, List<Boolean>> data = properties.entrySet()
						.parallelStream()
						.filter(e -> e.getKey().toString().startsWith(key))
						.collect(Collectors.toConcurrentMap(e -> e.getKey().toString(),
								e -> e.getValue().toString().isEmpty() ? Collections.emptyList()
										: Arrays.asList(e.getValue().toString().split(this.delimiter, -1))
												.stream()
												.map(Boolean::valueOf)
												.collect(Collectors.toList()),
								(a, b) -> b));// 重複キーがあれば後勝ち
				return Optional.ofNullable(data.isEmpty() ? null : data);
			}
		};
	}

	/**
	 * プロパティを指定された型へ変換する。
	 * 
	 * @param properties プロパティ
	 * @param key        キー
	 * @return プロパティ値
	 */
	public abstract Optional<T> parse(Properties properties, String key);

	/**
	 * 区切り文字を設定する。デフォルトはセミコロン（;）。
	 * 
	 * @param delimiter 区切り文字
	 * @return this
	 */
	public PropertyType<T> delimitWith(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

}
