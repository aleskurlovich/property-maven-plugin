<h1>property-maven-plugin</h1>

<p>A plugin for setter/getter auto generation. It uses <a href="http://www.csg.ci.i.u-tokyo.ac.jp/~chiba/javassist/">Javaassist</a> library
for generation and adding new bytecode.</p>

<p>Usage:</p>
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

