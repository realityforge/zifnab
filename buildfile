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

  define 'hdf' do
    pom.dependency_filter = Proc.new { |_| false }
    compile.with PACKAGED_DEPS

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.with :gir
    test.options[:java_args] = %w(-ea)
  end

  define 'core' do
    pom.dependency_filter = Proc.new { |_| false }
    compile.with project('hdf').package(:jar),
                 project('hdf').compile.dependencies

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.with :gir
    test.options[:java_args] = %W(-ea -Dzifnab.endless_sky_dir=#{ENV['ENDLESS_SKY_DIR'] || project._('../../endless-sky')})
  end

  define 'example' do
    compile.with project('core').package(:jar),
                 project('core').compile.dependencies
  end

  ipr.add_default_testng_configuration(:jvm_args => "-ea  -Dzifnab.endless_sky_dir=#{ENV['ENDLESS_SKY_DIR'] || project._('../endless-sky')}")

  iml.excluded_directories << project._('tmp')
  ipr.extra_modules << 'example/example.iml'
  ipr.extra_modules << '../endless-sky/endless-sky.iml'
  ipr.extra_modules << '../endless-sky-editor/endless-sky-editor.iml'

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_component('JavaProjectCodeInsightSettings') do |xml|
    xml.tag!('excluded-names') do
      xml << '<name>com.sun.istack.internal.NotNull</name>'
      xml << '<name>com.sun.istack.internal.Nullable</name>'
      xml << '<name>org.jetbrains.annotations.Nullable</name>'
      xml << '<name>org.jetbrains.annotations.NotNull</name>'
      xml << '<name>org.testng.AssertJUnit</name>'
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
