property-maven-plugin
=====================

A plugin for setter/getter auto generation.

Usage:
<pre>
&lt;build&gt;
        &lt;plugins&gt;
            &lt;plugin&gt;
                &lt;groupId&gt;plugins&lt;/groupId&gt;
                &lt;artifactId&gt;property-maven-plugin&lt;/artifactId&gt;
                &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
                &lt;executions&gt;
                    &lt;execution&gt;
                        &lt;id&gt;property-maven-plugin&lt;/id&gt;
                        &lt;phase&gt;process-classes&lt;/phase&gt;
                        &lt;goals&gt;
                            &lt;goal&gt;generate&lt;/goal&gt;
                        &lt;/goals&gt;
                    &lt;/execution&gt;
                &lt;/executions&gt;
            &lt;/plugin&gt;
        &lt;/plugins&gt;
    &lt;/build&gt;
</pre>

