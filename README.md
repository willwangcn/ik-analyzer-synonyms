ik-analyzer-synonym
===================

## What is ik-analyzer-synonym

ik-analyzer-synonym is a Chinese Analyzer for Lucene with synonym function.

## How to add dependency in your project

add the following config in your pom.xml

``` xml
<dependencies>
    <dependency>
        <groupId>com.willwang.lucene</groupId>
        <artifactId>ik-analyzer-synonym</artifactId>
        <version>4.9.0</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>ik-analyzer</id>
        <url>https://github.com/willwangcn/mvn-repo/</url>
    </repository>
</repositories>
```


## How to use

在ik-analyzer的基础上增加同义词filter，可以在索引时将同义词索引。
新增的类IKSynonymAnalyzer
同义词库放在data/synonyms.txt

#### data/synonyms.txt 配置方法
##### 通过,分割的可拓展同义词
“采取,采用,采纳”代表着这三个词是同义词，并且任何一个词可以被扩展为其他三个。如果expand设为false的话，则这三个词都会被统一替换为第一个词，也就是“采取”。

##### 通过=>收缩的不可拓展同义词
比如“采用,采纳  => 采取”代表这三个词同义，并且无视expand参数，统一会被替换为“采取”

默认是Lucene 4.9版本,使用其他版本请重新编译

``` java

public static void main(String[] args){
    //构建IK同义词分词器，使用smart分词模式
    Analyzer analyzer = new IKSynonymAnalyzer(Version.LUCENE_4_9, true);
    
    //获取Lucene的TokenStream对象
    TokenStream ts = null;
    try {
        ts = analyzer.tokenStream("myfield", new StringReader("物理老师数学数学老师"));
        //获取词元位置属性
        OffsetAttribute  offset = ts.addAttribute(OffsetAttribute.class); 
        //获取词元文本属性
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        //获取词元文本属性
        TypeAttribute type = ts.addAttribute(TypeAttribute.class);
        
        
        //重置TokenStream（重置StringReader）
        ts.reset(); 
        //迭代获取分词结果
        while (ts.incrementToken()) {
          System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
        }
        //关闭TokenStream（关闭StringReader）
        ts.end();   // Perform end-of-stream operations, e.g. set the final offset.

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        //释放TokenStream的所有资源
        if(ts != null){
          try {
            ts.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
    }
}
```