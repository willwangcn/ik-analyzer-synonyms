package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.util.FilesystemResourceLoader;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created : 14-9-22
 * Author : Will Wang
 * Description :
 * 使用IK分词器分词，增加同义词的filter
 *
 */
public class IKSynonymAnalyzer extends Analyzer {
    private boolean useSmart;
    private final Version matchVersion;


    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKSynonymAnalyzer() {
        this(Version.LUCENE_4_9, false);
    }

    public IKSynonymAnalyzer(Version matchVersion, boolean useSmart) {
        super();
        this.matchVersion = matchVersion;
        this.useSmart = useSmart;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, final Reader in) {
        Tokenizer tokenizer = new IKTokenizer(in, this.useSmart());
        Map<String, String> filterArgs = new HashMap<String, String>();
        filterArgs.put("luceneMatchVersion", matchVersion.toString());
        filterArgs.put("synonyms", "data/synonyms.txt");
        filterArgs.put("expand", "true");
        SynonymFilterFactory factory = new SynonymFilterFactory(filterArgs);
        try {
            factory.inform(new FilesystemResourceLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TokenStreamComponents(tokenizer, factory.create(tokenizer));
    }
}
