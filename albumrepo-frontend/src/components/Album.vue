<template>
  <div class="col-md-3 col-sm-6 col-xs-12">
    <q-card class="my-card">
      <q-img v-bind:src="`data:${this.type};base64,${this.picture}`" :ratio="1"/>

      <q-card-section class="q-pb-none">
        <div class="text-h6">{{ title }}</div>
        <div class="text-subtitle2">{{ artist }}</div>
      </q-card-section>

      <q-card-actions class="q-pt-none">
        <q-space />
        <q-btn
          color="primary"
          round
          flat
          dense
          :icon="expandedpanel ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
          v-on:click="expand"
        />
      </q-card-actions>

      <q-slide-transition>
        <div v-show="expandedpanel">
          <q-separator />
          <q-card-section class="text-subitle2">{{
            description
          }}</q-card-section>
          <q-card-section class="text-subitle2 q-pt-none">{{
            formatGenres()
          }}</q-card-section>
        </div>
      </q-slide-transition>
    </q-card>
  </div>
</template>
<script>
export default {
  name: 'Album',

  data () {
    return {
      expandedpanel: false
    }
  },
  props: ['item', 'id', 'description', 'picture', 'title', 'artist', 'type', 'genres'],
  methods: {
    expand () {
      this.expandedpanel = !this.expandedpanel
    },
    formatGenres () {
      return 'Genres: ' + this.genres.join(', ')
    }
  },
  filters: {
  }
}
</script>
<style lang="sass" scoped>
.my-card
  width: 100%
  max-width: 350px

.price-caption
  position: absolute
  bottom: 10px
  left: 10px
  background-color: black
  border-radius: 5px
  height: auto
  width: auto
  padding: 5px
</style>
