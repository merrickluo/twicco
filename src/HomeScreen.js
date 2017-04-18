import React from 'react'
import {
  StyleSheet,
  View,
} from 'react-native'

import { connect } from 'react-redux'
import { actions } from './reducers/timelines/home.js'

import BaseScreen from './BaseScreen.js'
import TweetList from './components/TweetList.js'

const mapStateToProps = (state) => {
  return {
    timeline: state.timelines.home,
    api: state.api.twitter.rest,
  }
}
const mapDispatchToProps = (dispatch) => {
  return {
    reload: (tweets) => { dispatch({ type: actions.reload, tweets }) }
  }
}

@connect(mapStateToProps, mapDispatchToProps)
export default class HomeScreen extends BaseScreen {
  constructor() {
    super()
  }

  componentDidMount() {
    // do not reload if cache is here
    // prevent over api limit
    if (!this.props.timeline.tweets.length) {
      const { api } = this.props
      api.get('statuses/home_timeline')
        .then(result => {
          this.props.reload(result)
        })
        .catch(e => { console.log(e) })
    }
  }

  render() {
    console.log(this.props)
    return (
      <View style={styles.container}>
        <TweetList tweets={this.props.timeline.tweets} />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  content: {
    flex: 1,
  },
})
