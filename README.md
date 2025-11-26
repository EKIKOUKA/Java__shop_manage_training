# Reactショップ管理システムのバックエンド、基本的な CRUD 機能（商品、ユーザー情報の追加・編集・削除・取得など）を提供しています、ただの練習作品。
### 起動コマンド GradlekのbootJar、ビルドコマンド Gradlekのbuild

## 🛠️ スタック紹介

  <ul>
    <li>バックエンドフレームワーク：Spring Boot</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;高速かつ堅牢な Java ベースのWebアプリケーションフレームワークです。</p>
    <li>ビルドツール：Gradle</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;依存関係の管理やプロジェクト構成、ビルド・実行を行うために使用。</p>    
    <li>データベース：MySQL</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;データ管理のために使用。</p>
    <li>認証トークン管理：JWT（Java JSON Web Token）</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;トークンの発行・検証を行い、ログイン状態の管理を実現。</p>
    <li>2段階認証（2FA）：GoogleAuthenticator + ZXing</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`googleauth` でTOTPを生成し、`zxing` ライブラリで QRコードを作成してユーザーに表示。</p>
    <li>パスワードを安全に扱う：BCrypt（Spring Security）</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;パスワードのセキュリティを向上させるための強力なツール</p>
    <li>ファイルアップロード処理：Spring MVC Multipart</li>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;画像などのファイルアップロードを処理するために、Spring Webのmultipart機能を使用。</p>
  </ul>

<img width="100%" src="https://github.com/user-attachments/assets/1bf79ee6-3b2b-422c-a3c6-a98b75c7f57c" />
