require 'spec_helper'

RSpec.describe 'Ping Endpoint' do
  describe 'GET /ping' do
    let(:path) { "/ping" }

    def do_request(params = {})
      get path, params
    end

    it 'should return 200' do
      do_request
      expect(last_response.status).to eq 200
    end

    it 'should return pong' do
      do_request
      expect(last_response.body).to eq 'pong'
    end
  end
end
