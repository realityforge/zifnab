desc 'Read all data files from specified directory'
task 'example:read_all' do
  data_dir = ENV['DATA_DIR']
  raise "Must specify DATA_DIR environment variable to location of data files" unless data_dir
  example = Buildr.project('example')
  jar = example.package(:jar)
  jar.invoke
  cp = (example.compile.dependencies + [jar.to_s]).join(':')
  Java::Commands.java ['-cp', cp, 'zifnab.examples.ReadDataFormat'] + Dir["#{data_dir}/*.txt"]
end
