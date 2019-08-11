require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/top_level_generate_dir'

PACKAGED_DEPS = [:javax_annotation]

desc 'zifnab: Endless Sky Content Editing Tools'
define 'zifnab' do
  project.group = 'org.realityforge.zifnab'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/zifnab')
  pom.add_developer('realityforge', 'Peter Donald')

  pom.dependency_filter = Proc.new { |_| false }
  compile.with PACKAGED_DEPS

  package(:jar)
  package(:sources)
  package(:javadoc)

  test.using :testng
  test.with :gir, :guiceyloops
  test.options[:java_args] = %W(-ea -Dzifnab.data_dir=#{ENV['ENDLESS_SKY_DIR'] || project._('../endless-sky/data')})

  ipr.add_default_testng_configuration(:jvm_args => '-ea ')

  iml.excluded_directories << project._('tmp')
  ipr.extra_modules << 'example/example.iml'
  ipr.extra_modules << 'endless-sky/endless-sky.iml'
  ipr.extra_modules << 'endless-sky-editor/endless-sky-editor.iml'

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_component('JavaProjectCodeInsightSettings') do |xml|
    xml.tag!('excluded-names') do
      xml << '<name>com.sun.istack.internal.NotNull</name>'
      xml << '<name>com.sun.istack.internal.Nullable</name>'
      xml << '<name>org.jetbrains.annotations.Nullable</name>'
      xml << '<name>org.jetbrains.annotations.NotNull</name>'
    end
  end
  ipr.add_component('NullableNotNullManager') do |component|
    component.option :name => 'myDefaultNullable', :value => 'javax.annotation.Nullable'
    component.option :name => 'myDefaultNotNull', :value => 'javax.annotation.Nonnull'
    component.option :name => 'myNullables' do |option|
      option.value do |value|
        value.list :size => '2' do |list|
          list.item :index => '0', :class => 'java.lang.String', :itemvalue => 'org.jetbrains.annotations.Nullable'
          list.item :index => '1', :class => 'java.lang.String', :itemvalue => 'javax.annotation.Nullable'
        end
      end
    end
    component.option :name => 'myNotNulls' do |option|
      option.value do |value|
        value.list :size => '2' do |list|
          list.item :index => '0', :class => 'java.lang.String', :itemvalue => 'org.jetbrains.annotations.NotNull'
          list.item :index => '1', :class => 'java.lang.String', :itemvalue => 'javax.annotation.Nonnull'
        end
      end
    end
  end
end

define 'example', :base_dir => "#{File.dirname(__FILE__)}/example" do
  compile.options.source = '1.8'
  compile.options.target = '1.8'

  compile.with project('zifnab').package(:jar),
               project('zifnab').compile.dependencies

  package(:jar)

  project.no_ipr
end

task('idea' => 'example:idea')
task('package' => 'example:package')
