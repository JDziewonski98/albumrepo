<template>
<q-layout class='bg-warning'>
      <form @submit.prevent="onSearch" class="q-pa-md">
      <q-input rounded outlined v-model="text" label="Search" class="q-pa-lg q-mx-sm q-mt-sm">
        <template v-slot:append>
          <q-btn name="cancel" class="cursor-pointer" />
        </template>
        <template v-slot:append>
          <q-icon name="search" class="cursor-pointer" @click="onSearch" />
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
            :genres="item.genres"
            :item="item"
          />
    </div>
  </div>
  <q-form @submit="createitem" class="q-gutter-lg q-pa-xl">
          <q-input filled v-model="title" label="Title" />
          <q-input filled v-model="artist" label="Artist" />
          <q-select
            filled
            v-model="genres"
            multiple
            :options="options"
            label="Genres"
          />
          <q-input filled v-model="description" label="Description" />
          <q-input
            filled
            stack-label
            v-model="photo"
            type="file"
            @change="onFileChanged"
            label="Cover Art"
          />
          <div style="text-align:center;">
            <q-btn
              label="Create Album"
              type="submit"
              style="background:#cad5db;"
            />
          </div>
  </q-form>
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
      ],
      title: null,
      artist: null,
      genres: null,
      description: null,
      photo: null,
      newPic: null,
      oldPic: null
    }
  },
  methods: {
    getAlbums () {
      console.log('here')
      this.$axios
        .get('getall/')
        .then(response => {
          const data = response.data
          this.items = []
          Object.keys(data).forEach(key => {
            console.log(key)
            this.items[key] = data[key]
          })
          this.items = this.items.sort((a, b) => a.id - b.id)
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
      if (this.text === '' && this.multiple == null) {
        this.getAlbums()
        return
      }
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
          this.items = []
          Object.keys(data).forEach(key => {
            console.log(key)
            this.items[key] = data[key]
          })
          this.items = this.items.sort((a, b) => a.id - b.id)
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
    onFileChanged (event) {
      this.newPic = event.target.files[0]
      this.oldPic = URL.createObjectURL(event.target.files[0])
    },
    createitem () {
      const formData = new FormData()
      formData.append('artist', this.artist)
      formData.append('description', this.description)
      formData.append('photo', this.newPic)
      formData.append('title', this.title)
      this.genres == null ? formData.append('genres', '') : formData.append('genres', this.genres.join(', '))
      formData.append('imgfile', this.newPic)

      this.$axios
        .post('upload/', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(resp => {
          this.$q.notify({
            color: 'green-4',
            position: 'top',
            textColor: 'white',
            icon: 'cloud_done',
            message: 'Successfully Created Album'
          })
          this.onSearch()
        })
        .catch(() => {
          this.$q.notify({
            color: 'red-4',
            position: 'top',
            textColor: 'white',
            icon: 'error',
            message: 'Something went wrong, please try again'
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
