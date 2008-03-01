#! ruby
require 'fileutils'
require 'webrick'
include WEBrick

SOURCES = ["src/com/scala_anim/*.scala", 
           "src/com/scala_anim/canvas/*.scala", 
           "src/com/scala_anim/event/*.scala",
           "src/com/scala_anim/geom/*.scala",
           "src/com/scala_anim/util/*.scala",
           "src/com/scala_anim/animation/*.scala"]
CLASS_PATH = ["classes"]

TEST_SOURCES = ["src/com/scala_anim/test/*.scala"]
TEST_CLASS_PATH = ["classes;classes/specs-1.1.4.jar;classes/junit-4.4.jar;classes/ScalaCheck-1.1.1.jar"]

task :clean => [] do
  FileUtils.rm_f Dir.glob('classes/*.class')
  FileUtils.rm_rf 'classes/com'
end

task :compile => [:clean] do
  system "fsc.bat -sourcepath src -classpath #{CLASS_PATH} -d classes #{(SOURCES).join(' ')}"
end

task :compile_once => [:clean] do
  system "scalac.bat -sourcepath src -classpath #{CLASS_PATH} -d classes #{(SOURCES).join(' ')}"
end

task :compile_tests => [:clean] do
  system "fsc.bat -classpath #{TEST_CLASS_PATH} -sourcepath src -d classes #{(SOURCES + TEST_SOURCES).join(' ')}"
end

task :test => [:clean, :compile_tests] do
  system "scala.bat -classpath #{TEST_CLASS_PATH} com.scala_anim.test.allSpecsRunner"
end

task :repl => [:compile] do
  PRELOAD = "import com.scala_anim._\nimport com.scala_anim.canvas._;"
  File.open(".repl_preload", "w+"){|f|
    f.write PRELOAD
  }
  system "scala.bat -classpath #{CLASS_PATH} -i .repl_preload"
  FileUtils.rm_rf '.repl_preload'
end

task :serve => [] do
  PORT = 8080
  s = HTTPServer.new(
                     :Port            => PORT,
                     :DocumentRoot    => Dir.pwd
                     )
  s.mount("/", HTTPServlet::FileHandler, Dir.pwd, true)
  trap("INT"){ s.shutdown }
  s.start
end

task :default => [:compile]
