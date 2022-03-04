package jp.co.hogehoge.framework.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Optional;

public enum CharCode {
	UTF8("UTF-8"),
	CP943C("x-IBM943C"),
	SJIS("Shift_JIS"),
	MS932("Windows-31J");

	private String name = null;
	private Charset charset = null;
	private CharsetDecoder decoder = null;
	private CharsetEncoder encoder = null;

	private CharCode(String name) {
		// 文字コード名
		this.name = name;
		// char set setting
		this.charset = Charset.forName(name);
		// decoder setting
		this.decoder = this.charset.newDecoder()
				.onMalformedInput(CodingErrorAction.REPORT) // 不正入力エラーのアクション
				.onUnmappableCharacter(CodingErrorAction.REPORT); // マップできない文字エラーに対するアクション
		// encoder setting
		this.encoder = this.charset.newEncoder()
				.onMalformedInput(CodingErrorAction.REPORT) // 不正入力エラーのアクション
				.onUnmappableCharacter(CodingErrorAction.REPORT); // マップできない文字エラーに対するアクション
	}

	/**
	 * デコーダー取得処理.
	 * 
	 * @return
	 */
	public CharsetDecoder getDecoder() {
		return this.decoder;
	}

	/**
	 * エンコーダー取得処理.
	 * 
	 * @return
	 */
	public CharsetEncoder getEncoder() {
		return this.encoder;
	}

	/**
	 * 文字コード名称から文字コード定義を取得する.
	 * 
	 * @param charCodeName
	 * @return
	 */
	public static Optional<CharCode> get(String charCodeName) {
		return Arrays.asList(CharCode.values()).stream().filter(c -> c.name.equals(charCodeName)).findFirst();
	}

}
