require 'spec_helper'
require 'json'
RSpec.describe 'Not Found Api' do
  describe 'GET /lists' do
    let(:path) { "/lists" }

    def do_request(params = {})
      get path, params
    end

    context 'when no params' do
      it 'should return 200' do
        do_request
        expect(last_response.status).to eq 200
      end

      it 'should return nochevieja and nochebuena' do
        do_request
        lists = JSON.parse(last_response.body)
        expect(lists.count).to eq(2)
        expect(lists.map { |l| l['name'] }.sort)
          .to eq(['NOCHEVIEJA', 'NOCHEBUENA'].sort)
      end
    end

    context 'when name=nochevieja' do
      it 'should return 200' do
        do_request({ name: 'nocHEVIeja'})
        expect(last_response.status).to eq 200
      end

      it 'should return nochevieja' do
        do_request({ name: 'nocHEVIeja'})
        lists = JSON.parse(last_response.body)
        expect(lists.count).to eq(1)
        expect(lists.first['name']).to eq('NOCHEVIEJA')
        expect(lists.first['items'].count).to eq(6)
      end
    end

    context 'when name=nochebuena' do
      it 'should return 200' do
        do_request({ name: 'nochebuENA'})
        expect(last_response.status).to eq 200
      end

      it 'should return nochevieja' do
        do_request({ name: 'nochebuENA'})
        lists = JSON.parse(last_response.body)
        expect(lists.count).to eq(1)
        expect(lists.first['name']).to eq('NOCHEBUENA')
        expect(lists.first['items'].count).to eq(7)
      end
    end

    context 'when name=pericopepe' do
      it 'should return 200' do
        do_request({ name: 'pericopepe'})
        expect(last_response.status).to eq 200
      end

      it 'should return empty array' do
        do_request({ name: 'pericopepe'})
        lists = JSON.parse(last_response.body)
        expect(lists).to eq([])
      end
    end
  end
end
