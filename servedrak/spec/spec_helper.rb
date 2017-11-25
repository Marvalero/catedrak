require_relative '../lib/servedrak'
require_relative 'support/app_helper'
require 'rspec'
require 'pry'
require 'rack/test'

module Servedrak
  module SpecHelper
    def self.configure_rspec
      RSpec.configure do |c|
        c.disable_monkey_patching!
        c.color = true
        c.include SpecHelper
        c.include Rack::Test::Methods
        c.include Servedrak::AppHelper
      end
    end
  end
end

Servedrak::SpecHelper.configure_rspec
