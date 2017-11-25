module Treefier
  module Base
    def initialize(parent)
      self.parent = parent
    end

    def method_missing(name, *args, &block)
      super unless parent.respond_to? name
      parent.send(name, *args, &block)
    end

    def respond_to_missing?(name, include_private = false)
      parent.respond_to?(name) or super
    end

    private
    attr_accessor :parent
  end
end

