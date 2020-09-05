<template>
<q-layout class='bg-warning'>
      <form @submit.prevent="onSearch" class="q-pa-md">
      <q-input rounded outlined v-model="text" label="Search" class="q-pa-lg q-mx-sm q-mt-sm">
        <template v-slot:append>
          <q-btn name="cancel" class="cursor-pointer" />
        </template>
        <template v-slot:append>
          <q-icon name="search" />
        </template>
      </q-input>
    </form>
    <div class="row reverse q-mr-xl items-center">
    <q-toggle
      v-model="value"
      color="blue"
    />
    <p class="text-caption q-mb-none">Match Genres Exactly?<p/>
    <q-select
      borderless
      clearable
      v-model="multiple"
      multiple
      :options="options"
      type="text"
      label="Genres:"
      style="width:15%"
      class="float-right q-mx-lg"
    >
      <template v-if="clearData" v-slot:append>
        <q-icon name="cancel" @click.stop="clearData = null" class="cursor-pointer" />
      </template>
    </q-select>
    </div>
  <div class="container">
    <div class="q-pa-md row justify-center items-start q-gutter-md">
      <Album
            style="margin: 10px"
            v-for="item in items"
            v-bind:key="item.id"
            :artist="item.artist"
            :title="item.title"
            :id="item.id"
            :description="item.description"
            :type="item.type"
            :picture="item.picture"
            :item="item"
          />
    </div>
  </div>
</q-layout>
</template>

<script>
import Album from '../components/Album.vue'
export default {
  data () {
    return {
      multiple: null,
      clearData: null,
      items: null,
      text: '',
      value: false,
      options: [
        'Pop',
        'Rock',
        'Metal',
        'Jazz',
        'Classical'
      ]
    }
  },
  methods: {
    getAlbums () {
      console.log('here')
      this.$axios
        .get('getall/')
        .then(response => {
          const data = response.data
          console.log(data)
          this.items = []
          Object.keys(data).forEach(key => {
            console.log(key)
            this.items[key] = data[key]
          })
          console.log(this.items)
        })
        .catch(() => {
          this.$q.notify({
            color: 'negative',
            position: 'top',
            message: 'Loading failed',
            icon: 'report_problem'
          })
        })
    },
    onSearch () {
      console.log(this.text)
      console.log(this.multiple)
      console.log(this.value)
      this.$axios
        .get('search/', {
          params: {
            text: this.text,
            genres: this.multiple != null ? this.multiple.join(',') : '',
            exactMatch: this.value
          }
        })
        .then(response => {
          const data = response.data
          console.log(data)
        })
        .catch(() => {
          this.$q.notify({
            color: 'negative',
            position: 'top',
            message: 'Loading failed',
            icon: 'report_problem'
          })
        })
    }
  },
  components: {
    Album
  },
  created () {
    this.getAlbums()
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
  justify-content: center;
}
</style>
