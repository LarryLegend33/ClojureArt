goog.addDependency("base.js", ['goog'], []);
goog.addDependency("../cljs/core.js", ['cljs.core'], ['goog.string', 'goog.Uri', 'goog.object', 'goog.math.Integer', 'goog.string.StringBuffer', 'goog.array', 'goog.math.Long']);
goog.addDependency("../process/env.js", ['process.env'], ['cljs.core']);
goog.addDependency("../cljsjs/p5/development/p5.inc.js", ['cljsjs.p5'], [], {'foreign-lib': true});
goog.addDependency("../quil/middlewares/deprecated_options.js", ['quil.middlewares.deprecated_options'], ['cljs.core']);
goog.addDependency("../clojure/string.js", ['clojure.string'], ['goog.string', 'cljs.core', 'goog.string.StringBuffer']);
goog.addDependency("../quil/util.js", ['quil.util'], ['cljs.core', 'clojure.string']);
goog.addDependency("../quil/sketch.js", ['quil.sketch'], ['goog.dom', 'cljs.core', 'quil.middlewares.deprecated_options', 'goog.object', 'goog.events.EventType', 'goog.style', 'goog.events', 'quil.util']);
goog.addDependency("../quil/core.js", ['quil.core'], ['cljsjs.p5', 'quil.sketch', 'cljs.core', 'clojure.string', 'quil.util']);
goog.addDependency("../quil/middlewares/navigation_3d.js", ['quil.middlewares.navigation_3d'], ['cljs.core', 'quil.core']);
goog.addDependency("../quil/middlewares/navigation_2d.js", ['quil.middlewares.navigation_2d'], ['cljs.core', 'quil.core']);
goog.addDependency("../quil/middlewares/fun_mode.js", ['quil.middlewares.fun_mode'], ['cljs.core', 'quil.core']);
goog.addDependency("../quil/middleware.js", ['quil.middleware'], ['cljs.core', 'quil.middlewares.navigation_3d', 'quil.middlewares.navigation_2d', 'quil.middlewares.fun_mode']);
goog.addDependency("../test_sketch/core.js", ['test_sketch.core'], ['cljs.core', 'quil.core', 'quil.middleware']);
