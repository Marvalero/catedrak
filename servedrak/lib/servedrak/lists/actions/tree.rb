module Servedrak
  module Lists
    module Actions
      class Tree
        include Treefier::Base

        def show
          @show ||= Peppaframe::BaseActions::Show.new(self)
        end
      end
    end
  end
end
