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
  test.options[:java_args] = %w(-ea)

  ipr.add_default_testng_configuration(:jvm_args => '-ea ')

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
end
