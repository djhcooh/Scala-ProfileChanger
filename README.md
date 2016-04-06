# Scala-ProfileChanger

##何だこれは
Twitterのprofileを自分にリプライ送って変更するやつです。

##どうやって使うの
1. config/config.scalaにアカウント名とか引っ掛けたい文字列とかを書き加える。
2. twitter.propertiesにトークンとか書き加える。
3. 固定したい文言があればProfileChanger.scalaのfooter辺りを書き換える。
4. sbtを入れてsbt runすればok(自分はjarファイルにしてバッググラウンドで動かしてます)
5. 勝手に落ちてても気にしない(今のところ落ちたことはありませんが)
